package com.androidyuan.aesjniencrypt;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidyuan.aesjni.AESEncrypt;
import com.androidyuan.aesjni.SignatureTool;

/**
 * Copyright (c) 2016. BiliBili Inc.
 * Created by wei on 17-3-1 ,email:602807247@qq.com
 */

public class TestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toast(AESEncrypt.encode(this, "1") + "");

        //toast(SignatureTool.getSignature(this)+"");

        try {
            test();
        }
        catch (Throwable throwable) {
            toast("error");
        }
        testBaseType();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void d(String s) {

        Log.d("TestModel", s);
    }



    private void testBaseType() {

        //1.会OOM
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//        }
//        {
//            byte[] mBytes1 = new byte[210 * 1024 * 1024];
//        }


        //2.会OOm
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//            mBytes=null;
//        }
//        System.gc();
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//            mBytes=null;
//        }
//        System.gc();


        //3. 会OOm
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//
//            int a=0;
//        }
//        System.gc();
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//
//            int a=0;
//        }
//        System.gc();


        //3. 不会OOm
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//            mBytes=null;
//            int a=0;
//        }
//        System.gc();
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//            mBytes=null;
//            int a=0;
//        }
//        System.gc();


        //3. 不会OOm  这里会自动触发GC
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//            mBytes=null;
//            int a=0;
//        }
//        {
//            byte[] mBytes = new byte[210 * 1024 * 1024];
//            mBytes=null;
//            int a=0;
//        }


    }

    private void test() throws Throwable {


        d("MAX_MEM:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");



        //1.不会OOM
//            new TestModel();
//            new TestModel();
//            new TestModel();
//            new TestModel();

        //2.会OOM
//            TestModel model = new TestModel();
//            TestModel model2 = new TestModel();
//            TestModel model3 = new TestModel();
//            TestModel model4 = new TestModel();



        // 3.这里无效的因为本身会自动出发 Full GC
//        TestModel model = new TestModel();
//        System.gc();
//        TestModel model2 = new TestModel();
//        System.gc();
//        TestModel model3 = new TestModel();
//        System.gc();
//        TestModel model4 = new TestModel();
//        System.gc();


        // 4.这里并没有效果 代码执行未离开这个作用域,这个作用域的所有字段都不会被释放

//        TestModel model = new TestModel();
//        System.gc();
//        TestModel model2 = new TestModel();
//        System.gc();
//        TestModel model3 = new TestModel();
//        System.gc();
//        TestModel model4 = new TestModel();
//        System.gc();


        //5.不会OOM   (因为作用域太小了,即使我没有主动调用GC,但是内存达到JVM参数配置的 警戒线会自动触发系统的GC)
//            {
//                TestModel model = new TestModel();
//            }
//            {
//                TestModel model2 = new TestModel();
//            }
//            {
//                TestModel model3 = new TestModel();
//            }
//            {
//                TestModel model4 = new TestModel();
//            }


        // 6.理论上来说null被java打包为字节码之后 在设备上运行会不断被 JIT即时编译系统 优化掉这行代码 android的 DVM就支持JIT,但是确实有效果了
//        TestModel model = new TestModel();
//        model=null;
//        TestModel model2 = new TestModel();
//        model2=null;
//        TestModel model3 = new TestModel();
//        model3=null;
//        TestModel model4 = new TestModel();
//        model4=null;


        //7.会OOM finalize 并没有什么卵用 java不推荐你用，但是你用了也没有效果根本释放不了的

        TestModel model = new TestModel();
        model.finalize();
        TestModel model2 = new TestModel();
        model.finalize();
        TestModel model3 = new TestModel();
        model.finalize();
        TestModel model4 = new TestModel();
        model.finalize();


    }

    @Override
    protected void finalize() throws Throwable {
        Log.d("TestModel", "act finalize.");
        super.finalize();
    }

    void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
