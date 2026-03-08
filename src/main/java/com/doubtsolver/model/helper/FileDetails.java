package com.doubtsolver.model.helper;

import com.doubtsolver.enums.TrainingStatus;

import lombok.Data;

@Data
public class FileDetails {

	private String fileName;
	private TrainingStatus trainingStatus;
	private String errorMessage;

}
