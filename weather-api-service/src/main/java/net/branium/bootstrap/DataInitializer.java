package net.branium.bootstrap;

import lombok.RequiredArgsConstructor;
import net.branium.location.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final LocationRepository locationRepo;

    @Override
    public void run(String[] args) throws Exception {
        
    }
}
