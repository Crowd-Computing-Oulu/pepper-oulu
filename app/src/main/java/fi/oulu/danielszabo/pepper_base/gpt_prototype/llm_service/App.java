package fi.oulu.danielszabo.pepper_base.gpt_prototype.llm_service;

public class App {

//    private static Scanner sc = new Scanner(System.in);
//
//    public static void main(String[] args) {
//
//        GPT35TurboPepperService llmService = new GPT35TurboPepperService(ApiConfig.API_TOKEN);
//
//        llmService.startConversation("test_conv1");
//
//        final String[] staticTopicOptions = { "[Help me with ]tell me about a person[ at the university].",
//                "[Help me with ]tell me about a place[ at the university].",
//                "[Help me with ]tell me about the university[ at the university].",
//                "[Tell me about ]A university facility.",
//                "Tell me about yourself." };
//
//        LLMResponseWithOptions response = new LLMResponseWithOptions("", staticTopicOptions);
//
//        String[] contextualOptions = staticTopicOptions;
//
//        String input;
//        while ((input = getUserInput(contextualOptions)) != null) {
//            switch (input) {
//                case "@exit":
//                    return;
//                case "@more": {
//                    llmService.extendOptions("test_conv1", input, response);
//                    break;
//                }
//                default: {
//                    response = llmService.respondTo("test_conv1", input);
//                    System.out.println("Pepper: " + response.getResponseText());
//                }
//            }
//            contextualOptions = response.getOptions();
//        }
//
//    }
//
//    private static String getUserInput(String... options) {
//
//        for (int i = 0; i < options.length; i++) {
//            System.out.println((i) + ", " + options[i].replaceAll("\\s*\\[[^\\]]*\\]\\s*", ""));
//        }
//
//        System.out.println("===\nM, More options");
//        System.out.print("G, Goodbye!\n> ");
//
//        String selected = sc.nextLine();
//
//        return switch (selected) {
//            case "m", "M" -> "@more";
//            case "g", "G" -> "@exit";
//            default -> options[Integer.valueOf(selected)].replaceAll("\\s*\\([^\\)]*\\)\\s*", "").replace("[", "")
//                    .replace("]", "");
//        };
//    }

}
