package myasm.com.myasm;

import android.util.Log;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AsmRegister {
    MethodVisitor methodVisitor;
    String inputTemplateClass;
    String searchTemplateClass;
    String templateConfig;
    String superClass;

    public void register(){
        String templatePath = "/sdcard/source/";
        superClass = "myasm/com/register/BaseTemplate";
        inputTemplateClass = templatePath + "InputTemplate.class";
        searchTemplateClass = templatePath + "SearchTemplate.class";
        templateConfig = templatePath+ "TemplateConfig.class";
        File configTemplateFile = new File(templateConfig);
        try {
            InputStream inputStream = new FileInputStream(configTemplateFile);
            ClassReader classReader = new ClassReader(inputStream);
            ClassWriter classWriter = new ClassWriter(classReader, 0);
            ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
                @Override
                public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
                    super.visit(i, i1, s, s1, s2, strings);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if(name.equals("<init>")) //只修改构造方法
                    methodVisitor = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
                        @Override
                        public void visitInsn(int opcode) {
                            if(opcode >= Opcodes.IRETURN && opcode<= Opcodes.RETURN){

                                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);//非静态方法加载this指针

                                methodVisitor.visitTypeInsn(Opcodes.NEW, inputTemplateClass); //new指令 创建对象
                                methodVisitor.visitInsn(Opcodes.DUP);//复制栈顶指针的值
                                methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, inputTemplateClass,//调用构造方法
                                        "<init>", "()V", false);

                                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, templateConfig,//调用指定register方法进行注册
                                        "register", "(L"+superClass+";)V" , false);
                            }
                            super.visitInsn(opcode);
                        }

                        @Override
                        public void visitMaxs(int i, int i1) {
                            super.visitMaxs(i+4, i1);
                        }
                    };
                    return methodVisitor;
                }
            };
            classReader.accept(classVisitor, 0);
            byte[] bytes = classWriter.toByteArray();
            Log.e("xxxxxxxx", "----------");

            File file = new File("/sdcard/dest/In.class");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }

}
