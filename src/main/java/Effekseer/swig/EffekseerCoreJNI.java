package Effekseer.swig;

import org.thesalutyt.storyverse.effekseer.Effekseer;
import org.thesalutyt.storyverse.effekseer.NativeUtils;

import java.io.IOException;
import java.util.Locale;

public class EffekseerCoreJNI {
  static {
    Effekseer.init();
    try {
      System.loadLibrary("EffekseerNativeForJava");
    } catch (UnsatisfiedLinkError e) {
      try {
        NativeUtils.loadLibraryFromJar("/" + System.getProperty("os.name").toLowerCase(Locale.ROOT) + "/" + System.getProperty("os.arch") + "/" + System.mapLibraryName("EffekseerNativeForJava"));
      } catch (IOException ioe) {
        throw new UnsatisfiedLinkError("Could not find Effekseer4J lib: "+ioe.getMessage());
      }
    }
  }
  public static native long new_EffekseerBackendCore();
  public static native void delete_EffekseerBackendCore(long jarg1);
  public static native int EffekseerBackendCore_GetDevice();
  public static native boolean EffekseerBackendCore_InitializeAsOpenGL();
  public static native void EffekseerBackendCore_Terminate();
  public static native long new_EffekseerEffectCore();
  public static native void delete_EffekseerEffectCore(long jarg1);
  public static native boolean EffekseerEffectCore_Load(long jarg1, EffekseerEffectCore jarg1_, byte[] jarg2, int jarg3, float jarg4);
  public static native String EffekseerEffectCore_GetTexturePath(long jarg1, EffekseerEffectCore jarg1_, int jarg2, int jarg3);
  public static native int EffekseerEffectCore_GetTextureCount(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native boolean EffekseerEffectCore_LoadTexture(long jarg1, EffekseerEffectCore jarg1_, byte[] jarg2, int jarg3, int jarg4, int jarg5);
  public static native boolean EffekseerEffectCore_HasTextureLoaded(long jarg1, EffekseerEffectCore jarg1_, int jarg2, int jarg3);
  public static native String EffekseerEffectCore_GetModelPath(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native int EffekseerEffectCore_GetModelCount(long jarg1, EffekseerEffectCore jarg1_);
  public static native boolean EffekseerEffectCore_LoadModel(long jarg1, EffekseerEffectCore jarg1_, byte[] jarg2, int jarg3, int jarg4);
  public static native boolean EffekseerEffectCore_HasModelLoaded(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native String EffekseerEffectCore_GetMaterialPath(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native int EffekseerEffectCore_GetMaterialCount(long jarg1, EffekseerEffectCore jarg1_);
  public static native boolean EffekseerEffectCore_LoadMaterial(long jarg1, EffekseerEffectCore jarg1_, byte[] jarg2, int jarg3, int jarg4);
  public static native String EffekseerEffectCore_GetCurvePath(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native boolean EffekseerEffectCore_HasMaterialLoaded(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native int EffekseerEffectCore_GetCurveCount(long jarg1, EffekseerEffectCore jarg1_);
  public static native boolean EffekseerEffectCore_LoadCurve(long jarg1, EffekseerEffectCore jarg1_, byte[] jarg2, int jarg3, int jarg4);
  public static native boolean EffekseerEffectCore_HasCurveLoaded(long jarg1, EffekseerEffectCore jarg1_, int jarg2);
  public static native int EffekseerEffectCore_GetTermMax(long jarg1, EffekseerEffectCore jarg1_);
  public static native int EffekseerEffectCore_GetTermMin(long jarg1, EffekseerEffectCore jarg1_);
  public static native long new_EffekseerManagerCore();
  public static native void delete_EffekseerManagerCore(long jarg1);
  public static native boolean EffekseerManagerCore_Initialize__SWIG_0(long jarg1, EffekseerManagerCore jarg1_, int jarg2, boolean jarg3);
  public static native boolean EffekseerManagerCore_Initialize__SWIG_1(long jarg1, EffekseerManagerCore jarg1_, int jarg2);
  public static native void EffekseerManagerCore_Update(long jarg1, EffekseerManagerCore jarg1_, float jarg2);
  public static native void EffekseerManagerCore_BeginUpdate(long jarg1, EffekseerManagerCore jarg1_);
  public static native void EffekseerManagerCore_EndUpdate(long jarg1, EffekseerManagerCore jarg1_);
  public static native void EffekseerManagerCore_UpdateHandleToMoveToFrame(long jarg1, EffekseerManagerCore jarg1_, int jarg2, float jarg3);
  public static native int EffekseerManagerCore_Play(long jarg1, EffekseerManagerCore jarg1_, long jarg2, EffekseerEffectCore jarg2_);
  public static native void EffekseerManagerCore_StopAllEffects(long jarg1, EffekseerManagerCore jarg1_);
  public static native void EffekseerManagerCore_Stop(long jarg1, EffekseerManagerCore jarg1_, int jarg2);
  public static native void EffekseerManagerCore_SetPaused(long jarg1, EffekseerManagerCore jarg1_, int jarg2, boolean jarg3);
  public static native void EffekseerManagerCore_SetShown(long jarg1, EffekseerManagerCore jarg1_, int jarg2, boolean jarg3);
  public static native void EffekseerManagerCore_SetEffectPosition(long jarg1, EffekseerManagerCore jarg1_, int jarg2, float jarg3, float jarg4, float jarg5);
  public static native void EffekseerManagerCore_SetEffectTransformMatrix(long jarg1, EffekseerManagerCore jarg1_, int jarg2, float jarg3, float jarg4, float jarg5, float jarg6, float jarg7, float jarg8, float jarg9, float jarg10, float jarg11, float jarg12, float jarg13, float jarg14);
  public static native void EffekseerManagerCore_SetEffectTransformBaseMatrix(long jarg1, EffekseerManagerCore jarg1_, int jarg2, float jarg3, float jarg4, float jarg5, float jarg6, float jarg7, float jarg8, float jarg9, float jarg10, float jarg11, float jarg12, float jarg13, float jarg14);
  public static native void EffekseerManagerCore_DrawBack(long jarg1, EffekseerManagerCore jarg1_);
  public static native void EffekseerManagerCore_DrawFront(long jarg1, EffekseerManagerCore jarg1_);
  public static native void EffekseerManagerCore_SetProjectionMatrix(long jarg1, EffekseerManagerCore jarg1_, float jarg2, float jarg3, float jarg4, float jarg5, float jarg6, float jarg7, float jarg8, float jarg9, float jarg10, float jarg11, float jarg12, float jarg13, float jarg14, float jarg15, float jarg16, float jarg17);
  public static native void EffekseerManagerCore_SetCameraMatrix(long jarg1, EffekseerManagerCore jarg1_, float jarg2, float jarg3, float jarg4, float jarg5, float jarg6, float jarg7, float jarg8, float jarg9, float jarg10, float jarg11, float jarg12, float jarg13, float jarg14, float jarg15, float jarg16, float jarg17);
  public static native boolean EffekseerManagerCore_Exists(long jarg1, EffekseerManagerCore jarg1_, int jarg2);
  public static native void EffekseerManagerCore_SetViewProjectionMatrixWithSimpleWindow(long jarg1, EffekseerManagerCore jarg1_, int jarg2, int jarg3);
  public static native void EffekseerManagerCore_SetDynamicInput(long jarg1, EffekseerManagerCore jarg1_, int jarg2, int jarg3, float jarg4);
  public static native float EffekseerManagerCore_GetDynamicInput(long jarg1, EffekseerManagerCore jarg1_, int jarg2, int jarg3);
  public static native void EffekseerManagerCore_LaunchWorkerThreads(long jarg1, EffekseerManagerCore jarg1_, int jarg2);
}
