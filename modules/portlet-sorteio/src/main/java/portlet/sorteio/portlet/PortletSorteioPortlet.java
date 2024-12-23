package portlet.sorteio.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import org.osgi.service.component.annotations.Component;
import portlet.sorteio.constants.PortletSorteioPortletKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.ProcessAction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Component(
        property = {
                "com.liferay.portlet.display-category=category.highlighted",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=false",
                "javax.portlet.display-name=PortletSorteio",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + PortletSorteioPortletKeys.PORTLETSORTEIO,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class PortletSorteioPortlet extends MVCPortlet {

    @ProcessAction(name = "sorteio")
    public void sorteios(ActionRequest request, ActionResponse response) {
        // Obtém os valores de entrada fornecidos pelo usuário no formulário
        String maxima = request.getParameter("final");
        String minima = request.getParameter("inicial");
        String quantidade = request.getParameter("quantidade");

        Random rand = new Random();

        try {
            // Verifica se algum parâmetro é nulo e lança uma exceção se for o caso
            if (maxima == null || minima == null || quantidade == null) {
                throw new IllegalArgumentException("Todos os parâmetros (final, inicial e quantidade) são obrigatórios.");
            }

            int maximaParams = Integer.parseInt(maxima);
            int minimaParams = Integer.parseInt(minima);
            int quantidadeParams = Integer.parseInt(quantidade);
            Random random = new Random();

            List<Integer> listaNumeros = new ArrayList<>();

            for (int i = 0; i < quantidadeParams; i++) {
                int numeroAleatorio = random.nextInt(maximaParams - minimaParams) + minimaParams;
                listaNumeros.add(numeroAleatorio);
            }

            request.setAttribute("listaNumeros", listaNumeros);

        } catch (NumberFormatException e) {
            e.printStackTrace(); // Captura e trata possíveis erros de conversão
            request.setAttribute("error", "Parâmetros devem ser números inteiros.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Captura o erro de parâmetro nulo
            request.setAttribute("error", e.getMessage());
        }

        // Obtém os nomes e a quantidade de nomes a serem sorteados
        String nomesParam = request.getParameter("nomes");

        // Verifica se os parâmetros são válidos
        if (nomesParam != null && !nomesParam.isEmpty() && quantidade != null && !quantidade.isEmpty()) {
            try {
                // Separa os nomes usando múltiplos delimitadores e remove espaços em branco
                String[] nomes = nomesParam.split("[,\\n]");
                List<String> itemList = new ArrayList<>();

                for (String nome : nomes) {
                    String trimmed = nome.trim();
                    if (!trimmed.isEmpty()) {
                        itemList.add(trimmed);
                    }
                }

                int quantidadeParams = Integer.parseInt(quantidade);

                // Ajusta a quantidade para não exceder o número de nomes disponíveis
                if (quantidadeParams > itemList.size()) {
                    quantidadeParams = itemList.size();
                }

                // Usa um Set para evitar duplicatas durante o sorteio
                Set<String> selectedName = new HashSet<>();
                Random random = new Random();

                while (selectedName.size() < quantidadeParams) {
                    int index = random.nextInt(itemList.size());
                    selectedName.add(itemList.get(index));
                }

                // Concatena os nomes sorteados em uma única string e define como atributo
                String randomNames = String.join(", ", selectedName);
                request.setAttribute("randomString", randomNames);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Captura e trata possíveis erros de conversão
            }
        }
//        Arquivo

        try {
            // Obtém o arquivo enviado pelo usuário
            UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(request);
            List<String> uploadedFileNames = new ArrayList<>();
            File fileTemp = uploadRequest.getFile("arquivos");

            if (fileTemp != null && fileTemp.exists()) {
                int quantidadeParams = Integer.parseInt(quantidade);
                String tempFilePath = fileTemp.getAbsolutePath();

                // Lê o arquivo e armazena cada linha em uma lista
                File myObj = new File(tempFilePath);
                ArrayList<String> list = new ArrayList<>();
                try (Scanner myReader = new Scanner(myObj)) {
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        String[] names = data.split(",");
                        for (String name : names) {
                            list.add(name.trim());
                        }
                    }
                }

                if (!list.isEmpty()) {
                    // Usa um Set para evitar nomes duplicados
                    Set<String> selectedNames = new HashSet<>();

                    while (selectedNames.size() < quantidadeParams && selectedNames.size() < list.size()) {
                        int index = rand.nextInt(list.size());
                        selectedNames.add(list.get(index));
                    }

                    // Adiciona os nomes selecionados à lista final
                    uploadedFileNames.addAll(selectedNames);
                } else {
                    System.out.println("A lista está vazia.");
                }
            } else {
                System.out.println("Nenhum arquivo encontrado!");
            }

            // Define a lista de nomes sorteados como atributo para o JSP
            request.setAttribute("randomName", uploadedFileNames);
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro ao tentar ler o arquivo.");
            e.printStackTrace(); // Captura e trata erros de arquivo não encontrado
        } catch (NumberFormatException e) {
            System.out.println("Erro: 'quantidade' não é um número válido.");
            e.printStackTrace(); // Captura e trata erros de conversão de número
        }


    }
}
