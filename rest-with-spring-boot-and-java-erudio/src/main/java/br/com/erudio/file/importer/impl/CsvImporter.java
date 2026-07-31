package br.com.erudio.file.importer.impl;

import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        Iterable<CSVRecord> record = format.parse(new InputStreamReader(inputStream));

        return parseRecordsToPersonDTOs(record);
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> record) {
        List<PersonDTO> dto = new ArrayList<>();

        for (CSVRecord reco : record) {
            PersonDTO person = new PersonDTO();
            person.setFirstName(reco.get("first_name"));
            person.setLastName(reco.get("last_name"));
            person.setAddress("address");
            person.setGender("gender");
            person.setEnabled(true);
            dto.add(person);
        }

        return dto;
    }
}
