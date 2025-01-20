package com.example.distributed_storage_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedStorageSystemLauncher {

	public static void main(String[] args) {
		SpringApplication.run(DistributedStorageSystemLauncher.class, args);
		System.out.println("Application started");
	}

}
