package br.com.petcare.domain.service;

import br.com.petcare.application.controller.exceptions.NaoEncontradoException;
import br.com.petcare.application.request.PetShopRequestDTO;
import br.com.petcare.application.response.PetShopResponseDTO;
import br.com.petcare.domain.entity.PetShop;
import br.com.petcare.domain.enums.TipoServicoEnum;
import br.com.petcare.infra.repository.PetShopRepository;
import br.com.petcare.infra.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class PetShopService {
    private final PetShopRepository petShopRepository;
    private final Utils utils;

    @Autowired
    public PetShopService(PetShopRepository petShopRepository, Utils utils) {
        this.petShopRepository = petShopRepository;
        this.utils = utils;
    }

    public Page<PetShopResponseDTO> buscarTodos(Pageable pageable, String nome, String cpf, String cnpj,
                                                Integer tipoServico) {
        Example<PetShop> example = Example.of(PetShop.builder()
                        .nome(nome)
                        .cpf(cpf)
                        .cnpj(cnpj)
                        .tipoServico(TipoServicoEnum.recuperarServico(tipoServico))
                        .build());

        Page<PetShop> prestadorServicoPage = petShopRepository.findAll(example, pageable);

        return prestadorServicoPage.map(this::toDTOResposta);
    }

    public PetShopResponseDTO cadastrar(PetShopRequestDTO petShopDTO) {
        PetShop petShop = toEntity(petShopDTO);

        return toDTOResposta(petShopRepository.save(petShop));
    }

    public PetShopResponseDTO atualizar(Integer idPetShop, PetShopRequestDTO petShopRequestDTO) {
        PetShop petShop = this.buscarPorId(idPetShop);
        PetShop request = toEntity(petShopRequestDTO);

        this.utils.copyNonNullProperties(request, petShop);

        return this.toDTOResposta(petShopRepository.save(petShop));
    }

    public void deletar(Integer idPetShop) {
        existePorId(idPetShop);

        petShopRepository.deleteById(idPetShop);
    }

    public PetShop buscarPorId(Integer idPetShop) {
        return this.petShopRepository.findById(idPetShop)
                .orElseThrow(() -> new NaoEncontradoException(
                        String.format("PetShop com o id '%d' não encontrado", idPetShop)));
    }

    public void existePorId(Integer idPetShop) {
        if(!petShopRepository.existsById(idPetShop))
            throw new NaoEncontradoException(
                    String.format("Pet Shop com o id '%d' não encontrado", idPetShop));
    }

    public PetShopResponseDTO toDTOResposta(PetShop petShop) {
        return new PetShopResponseDTO(
                petShop.getId(),
                petShop.getNome(),
                petShop.getCpf(),
                petShop.getCnpj(),
                petShop.getListaFuncionarios(),
                ObjectUtils.isEmpty(petShop.getTipoServico())
                        ? null : petShop.getTipoServico().getDescricao(),
                petShop.getEndereco()
        );
    }

    public PetShopRequestDTO toDTO(PetShop petShop) {
        return new PetShopRequestDTO(
                petShop.getId(),
                petShop.getNome(),
                petShop.getCpf(),
                petShop.getCnpj(),
                petShop.getListaFuncionarios(),
                ObjectUtils.isEmpty(petShop.getTipoServico())
                        ? null : petShop.getTipoServico().getId(),
                petShop.getEndereco()
        );
    }

    public PetShop toEntity(PetShopRequestDTO dto) {
        return PetShop.builder()
                .id(dto.id())
                .nome(dto.nome())
                .cpf(dto.cpf())
                .cnpj(dto.cnpj())
                .listaFuncionarios(dto.listaFuncionarios())
                .tipoServico(ObjectUtils.isEmpty(dto.tipoServico())
                        ? null : TipoServicoEnum.recuperarServico(dto.tipoServico()))
                .endereco(dto.endereco())
                .build();
    }
}
