package br.com.alura.servico;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.alura.dao.AgendamentoEmailDAO;
import br.com.alura.entidade.AgendamentoEmail;

@Stateless // indica que o objeto ejb será criado a cada chamada ao serviço
public class AgendamentoEmailServico 
{
	
	private static final Logger LOGGER = java.util.logging.Logger.getLogger(AgendamentoEmailServico.class.getName());
	
	@Inject // criado e gerenciado pelo servidor de aplicação
	private AgendamentoEmailDAO dao;
	
	public List<AgendamentoEmail> listar()
	{
		return dao.listar();
	}
	
	public void inserir(AgendamentoEmail agendamentoEmail)
	{
		agendamentoEmail.setAgendado(false);
		dao.inserir(agendamentoEmail);
	}
	
	public List<AgendamentoEmail> listarPorNaoAgendado()
	{
		return dao.listarPorNaoAgendado();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) // indica que este método não faz parte da transação
	public void alterar(AgendamentoEmail agendamentoEmail)
	{
		if (agendamentoEmail.getEmail().equals("joao@alura.com.br"))
		{
			throw new RuntimeException("falha provocada para teste de erro neste método");
		}
		else
		{
			agendamentoEmail.setAgendado(true);
			dao.alterar(agendamentoEmail);
		}
	}
	
	public void enviar(AgendamentoEmail agendamentoEmail)
	{
		
		try
		{
			Thread.sleep(5000);
			LOGGER.info("O email do usuário " + agendamentoEmail.getEmail() + " foi enviado");
		}
		catch (Exception e) 
		{
			LOGGER.warning(e.getMessage());
		}		
		
	}
	
}
