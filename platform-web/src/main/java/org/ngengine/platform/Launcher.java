// package org.ngengine.web;

// import java.lang.reflect.InvocationTargetException;

// import com.jme3.app.DebugKeysAppState;
// import com.jme3.app.FlyCamAppState;
// import com.jme3.app.SimpleApplication;
// import com.jme3.app.StatsAppState;
// import com.jme3.app.state.AppState;
// import com.jme3.app.state.ConstantVerifierState;
// import com.jme3.audio.AudioListenerState;
// import com.jme3.system.AppSettings;
// import com.jme3.system.JmeSystem;
// import com.jme3.util.BufferAllocatorFactory;
// import org.ngengine.web.context.HeapAllocator;
// import org.ngengine.web.context.WebSystem;
// import org.ngengine.web.demo.TestShadows;
// import org.ngengine.web.demo.TestInstancing;
// import org.ngengine.web.demo.TestAudio;
// import org.ngengine.web.demo.TestPBRSimple;
// import org.ngengine.web.demo.TestPhysics;

// public class WebApp {

//     public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
//             InstantiationException, NoSuchFieldException {

//         JmeSystem.setSystemDelegate(new WebSystem());


        

//         AppSettings settings = new AppSettings(true);
//         settings.setEmulateMouse(true);
//         settings.setWidth(1024);
//         settings.setResizable(true);
//         settings.setHeight(768);
//         settings.setGammaCorrection(true);
//         settings.setSamples(0);
//         settings.setFullscreen(false);
//         settings.setVSync(true);

//         AppState appStates[] = {

//             new StatsAppState(), new FlyCamAppState(), new AudioListenerState(), new DebugKeysAppState(),
//                 new ConstantVerifierState()
//          };

//         SimpleApplication app = null;

//         String demoToRun = args[0];

//         switch (demoToRun) {
//             default:
//             case "pbr":
//                 app = new TestPBRSimple(appStates);
//                 break;
//             case "shadows":
//                 app = new TestShadows(appStates);
//                 break;
//             case "physics":
//                 app = new TestPhysics(appStates);
//                 break;
//             case "audio":
//                 app = new TestAudio(appStates);
//                 break;
//             case "inst":
//                 app = new TestInstancing(appStates);
//                 break;
      
//         }

//         app.setSettings(settings);
//         app.start();

//     }
// }