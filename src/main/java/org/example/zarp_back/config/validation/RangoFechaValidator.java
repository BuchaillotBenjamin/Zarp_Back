package org.example.zarp_back.config.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.zarp_back.model.dto.reserva.ReservaDTO;

public class RangoFechaValidator implements ConstraintValidator<RangoFechaValido, ReservaDTO> {

    @Override
    public boolean isValid(ReservaDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        return !dto.getFechaFin().isBefore(dto.getFechaInicio());
    }
}
