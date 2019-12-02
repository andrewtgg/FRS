#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_hw_frsecurity_UpdateModelTask_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}