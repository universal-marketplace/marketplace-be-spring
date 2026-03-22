package com.example.universalmarketplacebe;

import org.springframework.boot.SpringApplication;

public class TestUniversalMarketplaceBeApplication {

	public static void main(String[] args) {
		SpringApplication.from(UniversalMarketplaceBeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
