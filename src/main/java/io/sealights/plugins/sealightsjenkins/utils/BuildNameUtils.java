package io.sealights.plugins.sealightsjenkins.utils;

import io.sealights.plugins.sealightsjenkins.BuildName;

/**
 * Created by Ronis on 5/18/2017.
 */
public class BuildNameUtils {

    public static String getManualBuildName(BuildName buildName) {
        BuildName.ManualBuildName manual = (BuildName.ManualBuildName) buildName;
        String insertedBuildName = manual.getInsertedBuildName();
        return insertedBuildName;
    }

}
