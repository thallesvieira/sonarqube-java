# sonarqube-java

Exemplo simples de como implementar sonarqube local com java utilizando docker.

Após abrir o projeto na sua IDE vamos começar a configurar.

# Rodando sonarqube pelo Docker
Entre no site do sonar: https://docs.sonarqube.org/latest/setup/get-started-2-minutes/ e copie o seguinte trecho:
![image](https://user-images.githubusercontent.com/31675029/179651129-9d2df4a6-3dcb-4415-b3a5-5ffaf71a7ce0.png)

Aqui podemos ver que o docker será rodado na porta 9000, este endereço está configurado no pom.xml do projeto.

Execute este código no seu terminal e o docker se encarregará de subir o sonar. 
Entre no endereço http://localhost:9000/ e coloco o login e senha (admin, admin), depois faça alteração de senha.
Neste exemplo vamos continuar com o login admin e inserir a senha 123.

# Configurando jacoco e sonarqube no pom.xml do projeto

Para que o sonarqube identifique o projeto é necessário adicionar o endereço local que o sonar rodará, o login e a senha, que foram criados, na tag properties do pom:

<sonar.host.url>http://localhost:9000</sonar.host.url>
<sonar.login>admin</sonar.login>
<sonar.password>123</sonar.password>

Como este exemplo é para utilização local e para aprendizado a senha ficará a mostra, mas vale lembrar que existem meios seguros para que 
os dados de login não fiquem evidentes.

Além destas, são necessárias outras informações como versão do jacoco, plugin, tipo de relatório que será gerado, local do relatório e linguagem utilizada: 

<jacoco.version>0.8.6</jacoco.version>
<sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
<sonar.dynamicAnalysis>reuseReports</sonar.dynamicAnalysis>
<sonar.jacoco.reportPath>${project.basedir}/../target/jacoco.exec</sonar.jacoco.reportPath>
<sonar.language>java</sonar.language>

![image](https://user-images.githubusercontent.com/31675029/179649489-e52fcc33-fd6d-40a4-affa-596e029ad604.png)

Dentro da tag dependencies está inserida a dependência do jacoco:

<dependency>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.6</version>
</dependency>

![image](https://user-images.githubusercontent.com/31675029/179649681-b5393e58-2bd4-4ae5-b27a-84151f2993fc.png)

Por fim, as informações de plugin do sonarqube e jacoco estão dentro da tag de plugins.

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


# Métodos e classe de testes com Junit

Para realizar alguns testes existe um exemplo básico para o funcionamento na classe Calculator.java, onde contém métodos de somar, subtrair, multiplicar e dividir:

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

A classe de teste é importante para que seja possível cobrir os métodos feitos.
Esta classe está representada com alguns testes simples dos métodos da classe Calculator:

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


Outro ponto é a exclusão de pacotes ou classes que não precisam de teste. 
Para excluir os pacotes que não fazem sentido cobrir com teste, neste caso uma única classe, foi inserida a tag "sonar.exclusions" dentro da tag properties, 
conforme exemplo abaixo:

<sonar.exclusions> src/main/java/com/sonar/sonarqube/SonarqubeApplication.java</sonar.exclusions>

Pronto, agora basta buildar o projeto e executar o comando 

mvn clean install sonar:sonar

Para verificar a cobertura entre no sonar: http://localhost:9000/, vá em Projetos, clique no nome do projeto e verifique o total de cobertura dos testes:
![image](https://user-images.githubusercontent.com/31675029/179652834-a0595b00-a5fa-4f25-8e53-fe8fccdf34a4.png)

Clicando no total de cobertura, neste exmplo 100, e depois na classe de teste, pode ser visualizado de forma separada a cobertura de cada teste:

![image](https://user-images.githubusercontent.com/31675029/179653022-85c369d6-9d6a-493c-b1fd-c3bd30ed234e.png)

Um exemplo simples e fácil para identificar a cobertura dos seus testes.
Isso pode ser utilizado com integração continua, em conjunto com CI, e garantir a cobertura dos testes no projeto onde existe uma equipe trabalhando.


