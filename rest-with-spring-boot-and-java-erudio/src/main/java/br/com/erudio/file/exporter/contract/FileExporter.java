package br.com.erudio.file.exporter.contract;

import br.com.erudio.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
