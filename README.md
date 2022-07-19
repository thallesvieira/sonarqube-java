# sonarqube-java

Exemplo simples de como implementar sonarqube local com java utilizando docker.
Inicialmente é necessário criar um projeto maven, com java (versão mínima 11) e spring boot.
Para facilitar o processo pode ser utilizado o site https://start.spring.io/.

Após criar o projeto e abrir na sua IDE vamos começar a configurar.

# Configurando jacoco e sonarqube no pom.xml do projeto

Para começar vamos adicionar o endereço local que o sonar rodará, o login e a senha, que ainda serão criados, na tag properties do pom:

<sonar.host.url>http://localhost:9000</sonar.host.url>
<sonar.login>admin</sonar.login>
<sonar.password>123</sonar.password>

Como este exemplo é para utilização local e para aprendizado a senha ficará a mostra, mas vale lembrar que temos outros meios seguros para que 
os dados de login não fiquem evidentes.

Além destas serão necessárias outras informações como versão do jacoco, plugin tipo de relatório que será gerado, local do relatório e linguagem utilizada: 

<jacoco.version>0.8.6</jacoco.version>
<sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
<sonar.dynamicAnalysis>reuseReports</sonar.dynamicAnalysis>
<sonar.jacoco.reportPath>${project.basedir}/../target/jacoco.exec</sonar.jacoco.reportPath>
<sonar.language>java</sonar.language>

![image](https://user-images.githubusercontent.com/31675029/179649489-e52fcc33-fd6d-40a4-affa-596e029ad604.png)

Agora, dentro da tag dependência deve ser inserido a dependência do jacoco:

<dependency>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.6</version>
</dependency>

![image](https://user-images.githubusercontent.com/31675029/179649681-b5393e58-2bd4-4ae5-b27a-84151f2993fc.png)

Por fim, é preciso adicionar o plugin, dentro da tag de plugins, o snarqube com a versão utilizada e as informações requiridas para o funcionamento do jacoco:

<plugin>
  <groupId>org.sonarsource.scanner.maven</groupId>
  <artifactId>sonar-maven-plugin</artifactId>
  <version>3.4.0.905</version>
</plugin>

<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>${jacoco.version}</version>
  <executions>
    <execution>
      <id>jacoco-initialize</id>
      <goals>
        <goal>prepare-agent</goal>
      </goals>
    </execution>
    <execution>
      <id>jacoco-site</id>
      <phase>package</phase>
      <goals>
        <goal>report</goal>
      </goals>
    </execution>
  </executions>
</plugin>

![image](https://user-images.githubusercontent.com/31675029/179650046-83cf061d-c6bb-4c33-8215-025d30e350c9.png)


# Criando métodos e classe de testes com Junit

Para realizar alguns testes vamos criar um exemplo básico para exemplificar o funcionamento.
Criar uma classe chamada Calculator.java e inserir métodos de somar, subtrair, multiplicar e dividir:

public class Calculator {

    public int sum(int a, int b) {
        return a+b;
    }

    public int subtract(int a, int b) {
        return a-b;
    }

    public int multiply(int a, int b) {
        return a*b;
    }

    public float divide(float a, float b) {
        return a/b;
    }

}

![image](https://user-images.githubusercontent.com/31675029/179650499-a995d576-5173-4172-9928-73d46fb25beb.png)

Agora selecione Ctrl+shift+T e vamos criar uma classe de teste para cobrir os métodos feitos.
A classe a seguir representa alguns testes simples da nossa classe Calculator:

package com.sonar.sonarqube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator calculator = new Calculator();

    @Test
    void sumTwoNumbers() {
       int result =  calculator.sum(5, 5);

       assertEquals(10, result);
    }

    @Test
    void subtractTwoNumbers() {
        int result =  calculator.subtract(12, 5);

        assertEquals(7, result);
    }

    @Test
    void multiplyTwoNumbers() {
        int result =  calculator.multiply(3, 5);

        assertEquals(15, result);
    }

    @Test
    void divideTwoNumbers() {
        float result =  calculator.divide(20, 5);

        assertEquals(4, result);
    }
}

![image](https://user-images.githubusercontent.com/31675029/179650812-1581bfa4-97ac-4a48-b171-e9e1a0992b9b.png)

Hora de rodar o sonarqube.

# Rodando sonarqube pelo Docker
Entre no site do sonar: https://docs.sonarqube.org/latest/setup/get-started-2-minutes/ e copie o seguinte trecho:
![image](https://user-images.githubusercontent.com/31675029/179651129-9d2df4a6-3dcb-4415-b3a5-5ffaf71a7ce0.png)

Aqui podemos ver que o docker será rodado na porta 9000, conforme definimos nas confirurações do pom.xml.

Execute este código no seu terminal e o docker se encarregará de subir o sonar. 
Entre no endereço http://localhost:9000/ e coloco o login e senha (admin, admin), depois faça alteração de senha.
Neste exemplo vamo continuar com o login admin e inserir a senha 123.


Pronto, vamos para a última parte.
Excluir os pacotes que não fazem sentido cobrir com teste.
Para este caso vamos remover dos testes apenas nossa classe de aplicação, sendo assim, vá no arquivo pom.xml e dentro da tag properties adicione a linha:

<sonar.exclusions> src/main/java/com/sonar/sonarqube/SonarqubeApplication.java</sonar.exclusions>

Agora basta buildar seu projeto e executar o comando 

mvn clean install sonar:sonar

Entre no sonar: http://localhost:9000/, ir em Projetos, clicar no nome do projeto e verificar o total de cobertura dos testes:
![image](https://user-images.githubusercontent.com/31675029/179652834-a0595b00-a5fa-4f25-8e53-fe8fccdf34a4.png)

Clicando no total de cobertura, neste exmplo 100, e depois na classe de teste, pode ser visualizado de forma separada a cobertura de cada teste:

![image](https://user-images.githubusercontent.com/31675029/179653022-85c369d6-9d6a-493c-b1fd-c3bd30ed234e.png)

Um exemplo simples e fácil para identificar a cobertura dos seus testes.
Isso pode ser utilizado com integração continua, em conjunto com CI, e garantir a cobertura dos testes no projeto onde existe uma equipe trabalhando.


