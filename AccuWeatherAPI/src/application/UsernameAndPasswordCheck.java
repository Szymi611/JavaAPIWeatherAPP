// &copy Piotr Stasiak Szymon Domagała

package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsernameAndPasswordCheck {
	private Map<String, String> users;
	private static final String FILE_NAME = "./user_data.txt";
	
	public UsernameAndPasswordCheck() {
		users = loadUserData(); //przy tworzeniu obiektu klasy od razu wczytujemy dane z pliku
		Main.demoLogger.debug("Successful loading UserData");
	}
	
	private Map<String, String> loadUserData(){
		Map<String, String> userData = new HashMap<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))){
				String line;
				while((line = bufferedReader.readLine()) != null) {
					String[] parts = line.split(":");
					userData.put(parts[0], parts[1]);
			}
		} catch (IOException e) {
			Main.demoLogger.error("Error loading user data from file.", e);
			
		}
		return userData;
	}
	
	private void saveUserData() {
		try (BufferedWriter bufferedWriter = new BufferedWriter (new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                bufferedWriter.write(entry.getKey() + ":" + entry.getValue());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
        	Main.demoLogger.error("Error saving user data to file.", e);
        }
	}
	
	public boolean registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            saveUserData(); // Save data after registration
            Main.demoLogger.info("Successful registration");
            return true; // Registration successful
        }
        return false; // Username already exists
    }
	
	public boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
	
}
