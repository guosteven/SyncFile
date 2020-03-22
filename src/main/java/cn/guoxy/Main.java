package cn.guoxy;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.log4j.Log4jLogFactory;
import cn.hutool.setting.Setting;

import java.util.List;

public class Main {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) {
        LogFactory factory = new Log4jLogFactory();
        LogFactory.setCurrentLogFactory(factory);
        Boolean isDebug = false;
        if (args.length == 1) {
            log.info("profile ={}", args);
            if ("debug".equals(args[0].toLowerCase())) {
                isDebug = true;
            }
        }
        Setting setting = new Setting("application.setting");
        List<String> groups = setting.getGroups();
        log.info("{} directories will be synchronized", groups.size());
        for (String group : groups) {
            String sourcePath = setting.get(group, "source.path");
            String distPath = setting.get(group, "dest.path");
            log.info("Initialization completed: the listener file directory is :" + sourcePath);
            log.info("Initialization completed: the target folder is : " + distPath);
            Runnable run = new SyncRunnable(sourcePath, distPath,isDebug);
            Thread thread = new Thread(run);
            thread.setName(group);
            thread.start();
        }


    }


}
