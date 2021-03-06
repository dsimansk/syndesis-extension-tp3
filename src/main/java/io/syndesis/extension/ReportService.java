package io.syndesis.extension;

import org.apache.camel.Body;
import org.apache.camel.Handler;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "reportService")
public class ReportService {

	@Handler
	public io.syndesis.extension.TodoReport generateReport(@Body io.syndesis.extension.InventoryList inventoryList) {

		List<Item> damagedItems = inventoryList.getItems().stream()
				.filter(Item::isDamaged)
				.collect(Collectors.toList());

		if (damagedItems.isEmpty()) {
			return null;
		} else {
			return new TodoReport(damagedItems);
		}
	}
}
