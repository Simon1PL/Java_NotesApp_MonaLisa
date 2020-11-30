package pl.edu.agh.monalisa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;

public class MonaLisaModule extends AbstractModule {

    @Provides
    @Singleton
    public FilesystemWatcher provideFilesystemListener() {
        return new FilesystemWatcher();
    }
}
