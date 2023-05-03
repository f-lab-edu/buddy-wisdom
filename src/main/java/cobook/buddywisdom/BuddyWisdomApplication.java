package cobook.buddywisdom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackageClasses = BuddyWisdomApplication.class)
public class BuddyWisdomApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuddyWisdomApplication.class, args);
    }

}
