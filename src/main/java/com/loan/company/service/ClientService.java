package com.loan.company.service;

import com.loan.company.Mapper.ClientMapper;
import com.loan.company.Mapper.LoanMapper;
import com.loan.company.dto.ClientDTO;
import com.loan.company.dto.ListClientDTO;
import com.loan.company.dto.LoanDTO;
import com.loan.company.dto.MessageResponseDTO;
import com.loan.company.entities.Client;
import com.loan.company.entities.Loan;
import com.loan.company.exception.ClientNotFoundException;
import com.loan.company.exception.LoanNotFoundException;
import com.loan.company.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClientService {

    private ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final LoanMapper loanMapper;


    public MessageResponseDTO create(ClientDTO clientDTO){

        System.out.println("Chegou: "+clientDTO);
        Client client = clientMapper.toModel(clientDTO);

        System.out.println(client);

        Client savedClient = clientRepository.save(client);


        MessageResponseDTO messageResponse = createMessageResponse("Client successfuly created with ID ", savedClient.getId());

        return messageResponse;


    }

    public MessageResponseDTO createLoan(ClientDTO clientDTO, LoanDTO loanDTO) throws ClientNotFoundException, LoanNotFoundException {


        Client savedClient = clientMapper.toModel(clientDTO);

        Loan savedLoan = loanMapper.toModel(loanDTO);



        if(Objects.nonNull(savedLoan.getClient())){
            savedLoan.getClient().getId();
        }

        savedClient.addLoan(savedLoan);
        savedLoan.setClient(savedClient);

        MessageResponseDTO messageResponse = createMessageResponse("Loan added successfuly with ID ", savedClient.getId());

        return messageResponse;


    }

    public List<ListClientDTO> listAll(){

        System.out.println("vai comerçar");
        List<Client> clients = clientRepository.findAll();

        System.out.println("MEUS CLIENTES " + clients);


        return clients.stream()
                .map(clientMapper::toListClientDTO)
                .collect(Collectors.toList());
    }

    public ClientDTO findById(Long id) throws ClientNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        return clientMapper.toDTO(client);
    }

    public MessageResponseDTO update(Long id, ClientDTO clientDTO) throws ClientNotFoundException {
        clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        Client updateClient = clientMapper.toModel(clientDTO);
        Client client = clientRepository.save(updateClient);

        MessageResponseDTO messageResponse = createMessageResponse("Client successfully updated with ID ", client.getId());

        return messageResponse;
    }

    public void delete(Long id) throws ClientNotFoundException {
        clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        clientRepository.deleteById(id);
    }



    private MessageResponseDTO createMessageResponse(String s, Long id){
        return MessageResponseDTO.builder()
                .message(s + id)
                .build();
    }
}
