package pl.edu.agh.monalisa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import pl.edu.agh.monalisa.loader.FilesystemWatcher;

import java.nio.file.Path;

public class MonaLisaModule extends AbstractModule {

    @Provides
    @Singleton
    public FilesystemWatcher provideFilesystemListener() {
        return new FilesystemWatcher();
    }

    @Override
    protected void configure() {
        bind(Path.class).annotatedWith(Names.named("RootPath")).toInstance(Path.of("MonaLisa"));
    }
}
