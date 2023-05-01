package com.mpdev.reporting.endtoend;

import com.mpdev.reporting.report.outreport.OutputItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReportTransformationEndToEndTest {

	@Autowired
	@SuppressWarnings("UnusedDeclaration")
	private JdbcTemplate jdbcTemplate;

	@Test
	@DisplayName("Database rows are created with correct values")
	void testFrontToBack() {
		List<OutputItem> outputList = jdbcTemplate.query("SELECT * FROM report",
				(rs, row) -> new OutputItem(
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8))
		);
		assertEquals(3, outputList.size());
		assertEquals("A.123.99", outputList.get(0).getContractId());
		assertEquals("MARIA", outputList.get(0).getFirstName());
		assertEquals("HALL", outputList.get(0).getLastName());
		assertEquals("P", outputList.get(0).getItemType());
		assertEquals("UK", outputList.get(0).getJurisdiction());
		assertEquals("B.448.85", outputList.get(1).getContractId());
		assertEquals("J", outputList.get(1).getFirstName());
		assertEquals("S", outputList.get(1).getLastName());
		assertEquals("S", outputList.get(1).getItemType());
		assertEquals("US", outputList.get(1).getJurisdiction());
		assertEquals("B.665.77", outputList.get(2).getContractId());
		assertEquals("URS", outputList.get(2).getFirstName());
		assertEquals("F", outputList.get(2).getLastName());
		assertEquals("C", outputList.get(2).getItemType());
		assertEquals("CH", outputList.get(2).getJurisdiction());
	}

}
