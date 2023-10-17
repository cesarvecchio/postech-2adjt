package br.com.petcare.dominio.dto;

import br.com.petcare.dominio.entidade.Dono;
import br.com.petcare.infra.enums.EspecieEnum;
import br.com.petcare.infra.enums.RacaEnum;

public record PetDTO(
         Integer id,
         String nome,
         EspecieEnum especie,
         RacaEnum raca,
         Dono dono
) {
}
