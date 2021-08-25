package br.com.alura.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.alura.entidade.AgendamentoEmail;
import br.com.alura.servico.AgendamentoEmailServico;

// para incluir o 'jar' do mysql
// na pasta bin do wildfly, execute o arquivo standalone.bat para executar o servidor
// executar:	jboss-cli.bat -c --controller=localhost:9990
// executar:	module add --name=com.mysql --resources="c:\hot\conectors\mysql-connector-java-8.0.20.jar" --dependencies=javax.api,javax.transaction.api
// executar:	/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-xa-datasource-class-name=com.mysql.cj.jdbc.MysqlXADataSource)
// acessar o servidor:	http://localhost:9990/console/index.html
// usuario: anderson, senha: r...sof.
// adicionar o datasource AgendamentoEmailDS / JNDI: java:/AgendamentoEmailDS
// jdbc:mysql://localhost:3306/agendamentoemaildb 

// para criar uma fila
// iniciar o wildfly
// acessar a pasta C:\hot\servidores\wildfly-20.0.1.Final\bin
// comandos: 
// jboss-cli.bat
// connect
// jms-queue add --queue-address=EmailQueue --entries=java:/jms/queue/EmailQueue


@WebServlet("emailsServlet")
public class AgendamentoEmailServlet extends HttpServlet 
{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AgendamentoEmailServico servico;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter pw = resp.getWriter();
		servico.listar().forEach(resultado -> pw.print("Os e-mails disponívels são: " + resultado.getEmail() ));
	}
	
	@Override
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		BufferedReader reader = req.getReader();
		String[] email = reader.readLine().split(",");
		AgendamentoEmail agendamentoEmail = new AgendamentoEmail();
		agendamentoEmail.setEmail(email[0]);
		agendamentoEmail.setAssunto(email[1]);
		agendamentoEmail.setMensagem(email[2]);
		servico.inserir(agendamentoEmail);
	}

}
