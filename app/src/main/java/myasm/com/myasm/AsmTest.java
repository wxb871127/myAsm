package myasm.com.myasm;

import android.util.Log;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * Created by wxb on 2018/8/30.
 */

public class AsmTest {
    public static void test() throws IOException {
        File srcFile = new File("/sdcard/Test.class");
        File destDir = new File("/sdcard/dest/");

        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        InputStream is = new FileInputStream(srcFile);
        byte[] bytes = referHackWhenInit(is);

        Log.e("xxxxx", bytes.toString());


        File destFile = new File("/sdcard/dest/Test2.class");
        if (!destFile.exists()){
            boolean r = destFile.createNewFile();
            Log.e("xxxxxxx", "create file " + r);
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        fos.write(bytes);
        fos.flush();
        fos.close();

    }

    private static byte[] referHackWhenInit(InputStream inputStream) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, final String name, String desc,
                                             String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if ("method1".equals(name)) {
                    Log.e("xxxxxxx", "find method1");


                    mv = new MethodVisitor(Opcodes.ASM5, mv) {
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode <= Opcodes.RETURN) {
//                                super.visitLdcInsn(Type.getType("Lcn/edu/zafu/hotpatch/asm/Hack"));
                                super.visitLdcInsn("hello asm");
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }
                return mv;
            }

        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }
    String a;
}
