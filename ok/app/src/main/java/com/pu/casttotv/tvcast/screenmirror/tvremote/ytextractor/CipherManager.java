package com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor;

import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.HTTPUtility;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.LogUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.ytextractor.utils.RegexUtils;
import java.io.IOException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

public class CipherManager {
    private static final String RegexDesipherFunctionCode = "\\{[a-zA-Z]{1,}=[a-zA-Z]{1,}.split\\(\"\"\\);[a-zA-Z0-9$]{2}\\.[a-zA-Z0-9$]{2}.*?[a-zA-Z]{1,}.join\\(\"\"\\)\\};";
    private static String RegexFindVarCode = "";
    private static final String RegexVarName = "[a-zA-Z0-9$]{2}\\.[a-zA-Z0-9$]{2}\\([a-zA-Z]\\,(\\d\\d|\\d)\\)";
    private static String cachedDechiperFunction;

    public static String getDecipherCode(String str) {
        String str2 = "decipher=function(a)" + RegexUtils.matchGroup(RegexDesipherFunctionCode, str);
        LogUtils.log("decfun=" + str2);
        String str3 = "var\\s" + RegexUtils.matchGroup(RegexVarName, str2).replace("$", "\\$").split("\\.")[0] + "=.*?\\};";
        RegexFindVarCode = str3;
        String str4 = str2 + "\n" + RegexUtils.matchGroup(str3, str);
        LogUtils.log("code= " + str4);
        return str4;
    }

    public static String dechiperSig(String str, String str2) throws IOException {
        if (cachedDechiperFunction == null) {
            cachedDechiperFunction = getDecipherCode(getPlayerCode(str2));
        }
        return RhinoEngine(str);
    }

    private static String getPlayerCode(String str) throws IOException {
        return HTTPUtility.downloadPageSource(str);
    }

    private static String RhinoEngine(String str) {
        Context enter = Context.enter();
        enter.setOptimizationLevel(-1);
        try {
            ScriptableObject initStandardObjects = enter.initStandardObjects();
            enter.evaluateString(initStandardObjects, cachedDechiperFunction, "JavaScript", 1, null);
            Object obj = initStandardObjects.get("decipher", initStandardObjects);
            if (obj instanceof Function) {
                return Context.toString(((Function) obj).call(enter, initStandardObjects, initStandardObjects, new Object[]{str}));
            }
            Context.exit();
            return str;
        } finally {
            Context.exit();
        }
    }
}
