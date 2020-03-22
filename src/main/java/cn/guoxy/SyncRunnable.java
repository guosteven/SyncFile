package cn.guoxy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.File;
import java.util.Objects;

/**
 * @author Guo Xiaoyong
 */
public class SyncRunnable implements Runnable {
    private static final Log log = LogFactory.get();
    private File sourceFile;
    private File distFile;
    private Boolean isDebug;

    public SyncRunnable(String source, String dist, Boolean isDebug) {
        sourceFile = new File(source);
        distFile = new File(dist);
        this.isDebug = isDebug;
    }

    @Override
    public void run() {
        while (true) {
            File[] files = sourceFile.listFiles();
            if (Objects.isNull(files)) {
                continue;
            }
            for (File file : files) {
                try {
                    syncFile(file, distFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }


    private void syncFile(File f, String dist) {
        try {
            if (f.isFile()) {
                String parpath = f.getParentFile().getAbsolutePath();

                String replace = StrUtil.replace(parpath, this.sourceFile.getAbsolutePath(), "", true);
                log.debug("{}", parpath);
                log.debug("{}", replace);
                File distDir;
                if (replace.isEmpty()) {
                    distDir = new File(dist);
                    if (!distDir.exists()) {
                        distDir.mkdirs();
                    }
                } else {
                    distDir = new File(dist + replace);
                    if (!distDir.exists()) {
                        distDir.mkdirs();
                    }
                }


                File re = FileUtil.copyFilesFromDir(f, distDir, true);
                if (!Objects.isNull(re)) {
                    log.info("sync {}  to  {}", f.getAbsolutePath(), distDir.getAbsolutePath());
                    boolean isDel = FileUtil.del(f);
                    if (isDel) {
                        log.info("delete {}", f.getAbsolutePath());
                    }
                }
            } else if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    syncFile(file, dist);
                }
                if (!isDebug) {
                    boolean isDel = FileUtil.del(f);
                    if (isDel) {
                        log.info("delete {}", f.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
