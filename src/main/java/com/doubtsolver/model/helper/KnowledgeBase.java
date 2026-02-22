package com.doubtsolver.model.helper;

import java.util.List;

import javax.management.loading.PrivateClassLoader;

import lombok.Data;

@Data
public class KnowledgeBase {
	private List<String> filesIDs;
	private List<Qna> qnas;
}
