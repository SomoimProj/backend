package com.oinzo.somoim.domain.user.entity;

import com.oinzo.somoim.common.type.Favorite;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;

public class FavoriteListConverter implements AttributeConverter<List<Favorite>, String> {
	private static final String SPLIT_CHAR = ";";

	@Override
	public String convertToDatabaseColumn(List<Favorite> favorites) {
		StringBuilder sb = new StringBuilder();
		for (Favorite favorite : favorites) {
			sb.append(favorite.name());
			sb.append(SPLIT_CHAR);
		}
		sb.deleteCharAt(sb.length() - 1);
		System.out.println("sb = " + sb);
		return sb.toString();
	}

	@Override
	public List<Favorite> convertToEntityAttribute(String string) {
		return Arrays.stream(string.split(SPLIT_CHAR))
			.map(Favorite::valueOfOrHandleException)
			.collect(Collectors.toList());
	}
}
