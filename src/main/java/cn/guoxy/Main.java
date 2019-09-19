package cn.guoxy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.dialect.Props;

import java.io.File;
import java.util.Objects;

public class Main {
    private static final Log log = LogFactory.get();
    public static void main(String[] args) {
        Props props = new Props("application.properties");
        String sourcePath = props.getStr("source.path");
        String distPath = props.getStr("dest.path");
        File sourceDir = new File(sourcePath);
        log.info("Initialization completed: the listener file directory is :" + sourcePath);
        log.info("Initialization completed: the target folder is : " + distPath);

        while (true){
            File[] files = sourceDir.listFiles();
            if (Objects.isNull(files)){
                continue;
            }
            for (File file : files) {
                syncFile(file, distPath);
            }
        }
    }

    private static void syncFile(File f,String dist) {
        try {
            File distDir = new File(dist);
            if (!distDir.exists()) {
                distDir.mkdirs();
            }
            if (f.isFile()) {
               File re =  FileUtil.copyFilesFromDir(f, distDir, true);
                if (!Objects.isNull(re)) {
                    log.info("File Sync success");
                    boolean isDel = FileUtil.del(f);
                    if (isDel){
                        log.info("delete source file ");
                    }
                }
            }
        }catch (Exception e){
            log.error(e);
        }
    }
}
