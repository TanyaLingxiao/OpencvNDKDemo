//
// Created by chenly on 2015/10/18.
//

#include <jni.h>


/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *   apps/samples/hello-jni/project/src/com/example/HelloJni/HelloJni.java
 */

jstring Java_com_chenly_opencvndkdemo_MainActivity_stringFromJNI( JNIEnv* env, jobject thiz )
{
    return (*env)->NewStringUTF(env, "FuckU from JNI !");
}
