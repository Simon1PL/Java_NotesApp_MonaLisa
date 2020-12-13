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
    // Jak działa to wyżej, bo ja bym to zrobił tak, a ta funkcja wyzej nie jest nigdzie wywolana?
    //    @Override
    //    protected void configure() {
    //        bind(FilesystemWatcher.class).in(Singleton.class);
    //    }

    @Override
    protected void configure() {
        bind(Path.class).annotatedWith(Names.named("RootPath")).toInstance(Path.of("MonaLisa"));
    }
}
