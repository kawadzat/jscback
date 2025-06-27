package io.getarrays.securecapita.codegenerator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorService {
    @Autowired
    private CodeGeneratorRepository codeGeneratorRepository;

    @Transactional
    public int generateCode(String type) {
        // Retrieve the sequence entry for the given identifier type
        CodeGenerator code = codeGeneratorRepository.findById(type).orElseGet(() -> {
            CodeGenerator newIdentifierSequence = new CodeGenerator();
            newIdentifierSequence.setType(type);
            newIdentifierSequence.setCurrentValue(0);
            codeGeneratorRepository.save(newIdentifierSequence);
            return newIdentifierSequence;
        });

        // Increment the identifier value and save it back
        int nextValue = code.getCurrentValue() + 1;
        code.setCurrentValue(nextValue);
        codeGeneratorRepository.save(code);

        return nextValue;
    }
}
