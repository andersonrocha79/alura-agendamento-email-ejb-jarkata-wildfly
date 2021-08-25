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

@Stateless // indica que é um EJB (a criação será de responsabilidade do servidor de aplicação)
@TransactionManagement(TransactionManagementType.BEAN) // indica que esta classe terá o controle manual de transação
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
			// transação controlada pelo usuário e não mais pelo container (servidor de aplicação)
			// seria um exemplo, porque o melhor é sempre manter a transação feita pelo 'container'
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
 
	código completo, quando não está utilizando @PersistenceContext 
 
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
