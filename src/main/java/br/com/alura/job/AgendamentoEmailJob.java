package br.com.alura.job;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import br.com.alura.entidade.AgendamentoEmail;
import br.com.alura.servico.AgendamentoEmailServico;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AgendamentoEmailJob 
{
	
	@Inject
	private AgendamentoEmailServico agendamentoEmailServico;
	
	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;
	
	@Resource(mappedName = "java:/jms/queue/EmailQueue")
	private Queue queue;
	
	// o m�todo ser� chamado somente uma vez, a cada 10 segundos
	// sem concorr�ncia, porque ter� apenas uma �nica inst�ncia do ejb, gerenciado pelo container
	@Schedule(hour = "*", minute = "*", second = "*/10")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public synchronized void enviarEmail() 
	{
		
		List<AgendamentoEmail> lista = agendamentoEmailServico.listarPorNaoAgendado();
		
		lista.forEach(emailNaoAgendado ->
		{
			// registra o email na fila
			context.createProducer().send(queue, emailNaoAgendado);
			// marca o email como agendado
			agendamentoEmailServico.alterar(emailNaoAgendado);
		});
		
	}

}
