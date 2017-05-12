package java.io.cbxn.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;


/**
 * Created by Apex on 22/02/2016.
 */
public class ELBTransformer implements IClassTransformer
{
    private final String ENTITY_LIVING_BASE = "EntityLivingBase";

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        boolean isObfuscated = !name.equals(transformedName);

        return transformedName.contains(ENTITY_LIVING_BASE)
                    ? transform(0, bytes, isObfuscated)
                    : bytes;
    }

    private byte[] transform(int index, byte[] classBeingTransformed, boolean isObfuscated)
    {
        try
        {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBeingTransformed);
            classReader.accept(classNode, 0);

            switch(index)
            {
                case 0:
                    transformEntityLivingBase(classNode, isObfuscated);
                    break;
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return classBeingTransformed;
    }

    private void transformEntityLivingBase(ClassNode classNode, boolean isObfuscated)
    {
        final String MOVEENTITYWITHHEADING = isObfuscated ? "e" : "func_70612_e";
        final String MOVEENTITYWITHHEADING_DESC = "(FF)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(MOVEENTITYWITHHEADING) && method.desc.equals(MOVEENTITYWITHHEADING_DESC))
            {
                AbstractInsnNode movefNode = null, motionxNode = null, motionzNode = null, motionyNode = null;
                for (AbstractInsnNode instruction : method.instructions.toArray())
                {
                    if (instruction.getOpcode() == ALOAD) {
                        if (((VarInsnNode)instruction).var == 0 && instruction.getNext().getOpcode() == DUP && motionxNode == null) {
                            motionxNode = instruction;
                        } else if (((VarInsnNode)instruction).var == 0 && instruction.getNext().getOpcode() == DUP && motionyNode == null) {
                            motionyNode = instruction;
                        } else  if (((VarInsnNode)instruction).var == 0 && instruction.getNext().getOpcode() == DUP && motionzNode == null) {
                            motionzNode = instruction;
                        } else if (movefNode == null){
                            if (((VarInsnNode)instruction).var == 0 && instruction.getNext().getOpcode() == FLOAD && ((VarInsnNode)instruction.getNext()).var == 1) {
                                movefNode = instruction;
                            }
                        }
                    }
                }

                if (motionxNode != null) {
                    /*
                    -    mv.visitVarInsn(ALOAD, 0);
                    -    mv.visitInsn(DUP);
                    -    mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "motionX", "D");
                    -    mv.visitLdcInsn(new Double("0.800000011920929"));
                    -    mv.visitInsn(DMUL);
                    -    mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "motionX", "D");

                    +    mv.visitVarInsn(ALOAD, 0);
                    +    mv.visitInsn(DUP);
                    +    mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "motionX", "D");
                    +    mv.visitMethodInsn(INVOKESTATIC, "io/cbxn/core/ApexCore", "getHMove", "()D", false);
                    +    mv.visitInsn(DMUL);
                    +    mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "motionX", "D");
                    */
                    for (int i = 0; i < 6; i++) {
                        motionxNode = motionxNode.getNext();
                        method.instructions.remove(motionxNode.getPrevious());
                    }

                    InsnList xList = new InsnList();

                    xList.add(new VarInsnNode(ALOAD, 0));
                    xList.add(new InsnNode(DUP));
                    xList.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/EntityLivingBase", "field_70159_w", "D"));
                    xList.add(new MethodInsnNode(INVOKESTATIC, "io/cbxn/core/ApexCore", "getHMove", "()D", false));
                    xList.add(new InsnNode(DMUL));
                    xList.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "field_70159_w", "D"));

                    method.instructions.insertBefore(motionxNode, xList);
                }
                if (motionzNode != null) {
                    /*
                    -    mv.visitVarInsn(ALOAD, 0);
                    -    mv.visitInsn(DUP);
                    -    mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "motionZ", "D");
                    -    mv.visitLdcInsn(new Double("0.800000011920929"));
                    -    mv.visitInsn(DMUL);
                    -    mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "motionZ", "D");

                    +    mv.visitVarInsn(ALOAD, 0);
                    +    mv.visitInsn(DUP);
                    +    mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", "motionZ", "D");
                    +    mv.visitMethodInsn(INVOKESTATIC, "io/cbxn/core/ApexCore", "getHMove", "()D", false);
                    +    mv.visitInsn(DMUL);
                    +    mv.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "motionZ", "D");
                     */
                    for (int i = 0; i < 6; i++) {
                        motionzNode = motionzNode.getNext();
                        method.instructions.remove(motionzNode.getPrevious());
                    }

                    InsnList zList = new InsnList();

                    zList.add(new VarInsnNode(ALOAD, 0));
                    zList.add(new InsnNode(DUP));
                    zList.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/EntityLivingBase", "field_70179_y", "D"));
                    zList.add(new MethodInsnNode(INVOKESTATIC, "io/cbxn/core/ApexCore", "getHMove", "()D", false));
                    zList.add(new InsnNode(DMUL));
                    zList.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/EntityLivingBase", "field_70179_y", "D"));

                    method.instructions.insertBefore(motionzNode, zList);
                }
                if (movefNode != null) {
                    for (int i = 0; i < 14; i++) {
                        movefNode = movefNode.getNext();
                        method.instructions.remove(movefNode.getPrevious());
                    }
                    /*
                    -    mv.visitVarInsn(ALOAD, 0);
                    -    mv.visitVarInsn(FLOAD, 1);
                    -    mv.visitVarInsn(FLOAD, 2);
                    -    mv.visitVarInsn(ALOAD, 0);
                    -    mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "isAIEnabled", "()Z", false);

                    -    mv.visitJumpInsn(IFEQ, l4);
                    -    mv.visitLdcInsn(new Float("0.04"));
                    -    mv.visitJumpInsn(GOTO, l5);
                    -    mv.visitLabel(l4);
                    -    mv.visitFrame(Opcodes.F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, Opcodes.FLOAT, Opcodes.DOUBLE}, 3, new Object[] {"net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, Opcodes.FLOAT});
                    -    mv.visitLdcInsn(new Float("0.02"));

                    -    mv.visitLabel(l5);
                    -    mv.visitFrame(Opcodes.F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, Opcodes.FLOAT, Opcodes.DOUBLE}, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, Opcodes.FLOAT, Opcodes.FLOAT});
                    -    mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "moveFlying", "(FFF)V", false);

                    +    mv.visitVarInsn(ALOAD, 0);
                    +    mv.visitVarInsn(FLOAD, 1);
                    +    mv.visitVarInsn(FLOAD, 2);
                    +    mv.visitMethodInsn(INVOKESTATIC, "io/cbxn/core/ApexCore", "getVMove", "()F", false);
                    +    mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "moveFlying", "(FFF)V", false);
                     */

                    InsnList moveList = new InsnList();

                    moveList.add(new VarInsnNode(ALOAD, 0));
                    moveList.add(new VarInsnNode(FLOAD, 1));
                    moveList.add(new VarInsnNode(FLOAD, 2));
                    moveList.add(new MethodInsnNode(INVOKESTATIC, "io/cbxn/core/ApexCore", "getVMove", "()F", false));
                    moveList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "func_70060_a", "(FFF)V", false));

                    method.instructions.insertBefore(movefNode, moveList);
                }
            }
        }
        System.out.println("[ApexCoreMod] Finished transforming EntityLivingBase.");
    }

}
