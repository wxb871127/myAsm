package myasm.com.myasm;

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


        File destFile = new File(destDir, "Test.class");
        FileOutputStream fos = new FileOutputStream(destFile);
        fos.write(bytes);
        fos.close();

    }

    private static byte[] referHackWhenInit(InputStream inputStream) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public MethodVisitor visitMethod(int access, final String name, String desc,
                                             String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if ("method1".equals(name)) {
                    mv = new MethodVisitor(Opcodes.ASM4, mv) {
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == Opcodes.RETURN) {
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
