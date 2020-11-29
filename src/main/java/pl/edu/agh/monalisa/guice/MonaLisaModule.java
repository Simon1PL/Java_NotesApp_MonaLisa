package pl.edu.agh.monalisa.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import pl.edu.agh.monalisa.model.Loader;

public class MonaLisaModule extends AbstractModule {

    @Provides
    public Loader provideLoader(){
        return new Loader();
    }
}
