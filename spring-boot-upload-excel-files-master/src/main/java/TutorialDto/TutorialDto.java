package TutorialDto;

import lombok.Data;

@Data
public class TutorialDto {
	private long id;
	private String title;
	private String description;
	private boolean published;
}
