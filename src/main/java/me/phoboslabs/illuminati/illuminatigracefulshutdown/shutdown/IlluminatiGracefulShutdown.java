package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown;


import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.annotation.IlluminatiGracefulShutdownImpl;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(IlluminatiGracefulShutdownImpl.class)
public @interface IlluminatiGracefulShutdown {
}