package br.com.alura.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import br.com.alura.entidade.AgendamentoEmail;

@Stateless // indica que � um EJB (a cria��o ser� de responsabilidade do servidor de aplica��o)
@TransactionManagement(TransactionManagementType.BEAN) // indica que esta classe ter� o controle manual de transa��o
public class AgendamentoEmailDAO 
{
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private UserTransaction userTransaction;
	
	public List<AgendamentoEmail> listar()
	{		
		return em.createQuery("SELECT ae FROM AgendamentoEmail ae", AgendamentoEmail.class).getResultList();		
	}
	
	public void inserir(AgendamentoEmail agendamentoEmail)
	{
		em.persist(agendamentoEmail);
	}
	
	public List<AgendamentoEmail> listarPorNaoAgendado()
	{		
		return em.createQuery("SELECT ae FROM AgendamentoEmail ae" + 
	                          " WHERE ae.agendado = false ", 
	                          AgendamentoEmail.class).getResultList();		
	}	
	
	public void alterar(AgendamentoEmail agendamentoEmail)
	{
		try
		{
			// transa��o controlada pelo usu�rio e n�o mais pelo container (servidor de aplica��o)
			// seria um exemplo, porque o melhor � sempre manter a transa��o feita pelo 'container'
			userTransaction.begin();
			em.merge(agendamentoEmail);
			userTransaction.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


/*
 
	c�digo completo, quando n�o est� utilizando @PersistenceContext 
 
	private EntityManager em; 
	public AgendamentoEmailDAO()
	{		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("AgendamentoEmailDS");
		this.em = emf.createEntityManager();		
	}
	
	public List<AgendamentoEmail> listar()
	{
		
		em.getTransaction().begin();
		List<AgendamentoEmail> resultado = em.createQuery("SELECT ae FROM AgendamentoEmail ae", AgendamentoEmail.class).getResultList();
		em.getTransaction().commit();
		em.close();
		
		return resultado;
		
	}
*/	

}
