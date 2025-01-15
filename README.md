# ForumHub
Desafio do ONE (Oracle Next Education).<br>
Um fórum é um espaço onde todos os participantes de uma plataforma podem colocar suas perguntas sobre determinados assuntos.

### OBJETIVO:
Desenvolver de uma API REST utilizando Spring com as seguintes funcionalidades:
- API com rotas implementadas seguindo as melhores práticas do modelo REST;
- Validações realizadas segundo as regras de negócio;
- Implementação de uma base de dados relacional para a persistência da informação; 
- Serviço de autenticação/autorização para restringir o acesso à informação.

## Classes
- ### Autenticação e Autorização
    É possível efetuar o login na aplicação utilizando tanto o nome de usuário como o e-mail cadastrado, contanto que este seja um usuário ativo utilizando o método
    `findActiveUserByUsernameOrEmail(usernameOrEmail)`.<br>
    As páginas de cadastro de usuário e login são as únicas que não exigem autorização e autenticação prévia para serem visualizadas.

- ### Modelos
    - `User`<br>
      Contém as informações do usuário.<br>
      Exige nome de usuário, e-mail e senha para realizar o cadastro.<br>
      Os campos nome de usuário e e-mail são unicos, e não podem ser repetidos dentro da base de dados.<br>
      Armazena também outras informações, como data de registro e estado de atividade.
    - `Topic`<br>
      Contém as informações de um tópico.<br>
      Exige o título do tópico, a mensagem, a categoria e o curso para realizar o cadastro.<br>
      Armazena também outras informações, como a ID do autor que criou o tópico e a data de criação.<br>
      Extende da classe `Content` com a anotação `@MappedSuperclass`, possuindo também informações sobre estado de atividade, caso o tópico já tenha sido editado, e a data da última edição.
    - `Reply`<br>
      Contém as informações de uma resposta/comentário a um tópico específico.<br>
      Exige o ID do tópico no link da requisição, e a mensagem a ser enviada no corpo para realizar o cadastro.<br>
      Armazena também outras informações, como a ID do autor da resposta, a ID do tópico e a data de criação da resposta.<br>
      Extende da classe `Content` com a anotação `@MappedSuperclass`, possuindo também informações sobre estado de atividade, caso o tópico já tenha sido editado, e a data da última edição.

- ### Controllers
    - `AuthenticationController`<br>
      Contém o método `login` que autentica o usuário ao receber informações de login e senha, podendo ser o login o nome de usuário, ou o e-mail cadastrado.
    - `UserController`<br>
      Contém as funções `registerUser`, para criar um novo cadastro de usuário, não exigindo autenticação prévia; 
      `listUsers`, para listar todos os usuários ativos cadastrados, este método é de acesso exclusivo para admins; 
      `detailUser`, para detalhar as informações de um usuário específico, este método é de acesso exclusivo para admins; 
      `deactivateUser` para desativar o cadastro de um usuário, este método só pode ser executado pelo próprio usuário para desativar a própria conta, ou por admins;
      `deactivateSelf` para o usuário desativar o próprio cadastro sem a necessidade de passar o próprio ID como argumento.
    - `TopicController`<br>
      Contém as funções `createTopic`, para registrar um novo tópico; 
      `detailTopic`, para detalhar as informações de um tópico específico; 
      `listTopics`, para listar todos os tópicos ativos cadastrados; 
      `editTopic`, permite ao autor de um tópico específico editar suas informações, podendo ser editadas opcionalmente as informações de título do tópico, a mensagem, a categoria e o curso do tópico; <br>
      `deactivateTopic`, para desativar um tópico, só pode ser executado pelo próprio autor do tópico, e por admins; <br>
      Este controller também permite o controle de respostas aos tópicos: <br> 
      `createTopicReply`, para cadastrar uma nova resposta a um tópico específico; 
      `detailTopicReply`, para detalhar as informações de uma resposta à um tópico específico; 
      `listTopicReplies`, para listar todos as respostas ativas de um tópico específico; 
      `editTopicReply`, permite ao autor de uma resposta à um tópico específico editar a mensagem de sua resposta; 
      `deactivateTopicReply`, para desativar uma resposta à um tópico específico, só pode ser executado pelo próprio autor da resposta, e por admins.
    - `ReplyController`<br>
      `createReply`, para cadastrar uma nova resposta a um tópico específico;
      `detailReply`, para detalhar as informações de uma resposta à um tópico específico;
      `listReplies`, para listar todos as respostas ativas de um tópico específico;
      `editReply`, permite ao autor de uma resposta à um tópico específico editar a mensagem de sua resposta;
      `deactivateReply`, para desativar uma resposta à um tópico específico, só pode ser executado pelo próprio autor da resposta, e por admins.<br>
      Estes métodos podem ser executados também diretamente pelo `TopicController`.

- ### Repositórios @Repository
    - `interface UserRepository`<br>
      Contém as funções `void register(String username, String email, String password)` que realiza o cadastro de um novo usuário no banco de dados utilizando uma query nativa;
      `User findByUsername(String username)` e `User findByEmail(String email)` que retornam usuários por seu nome de usuário e e-mail respectivamente; 
      `User findActiveUserByUsernameOrEmail(String usernameOrEmail)` que utiliza uma query nativa para procurar um usuário ativo pelo seu nome de usuário ou e-mail;
      e `Page<User> findAllByActiveTrue(Pageable pagination)` que retorna todos os usuários ativos do banco de dados.
    - `interface TopicRepository`<br>
      Contém a função `Page<Topic> findAllByActiveTrue(Pageable pagination)` que retorna todos os tópicos ativos.
    - `interface ReplyRepository`<br>
      Contém a função `Page<Reply> findAllByTopicIDAndActiveTrue(Long topicID, Pageable pagination)` que retorna todas as respostas ativas de um tópico específico.