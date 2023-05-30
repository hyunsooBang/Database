import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Welcome {

	static User mUser;
	static List<User> userList = new ArrayList<>();
	static ResultSet stuRs;
	static Statement stmt;
	static Connection conn;

	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/project", "root", "root12124");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "tjdwns246246");
			System.out.println("MySQL DB 연결 성공");

			// SQL 연결
			stmt = conn.createStatement();

			String sql = "select* from student";
			stuRs = stmt.executeQuery(sql);

			while (stuRs.next()) {
				String id = stuRs.getString("id");
				String name = stuRs.getString("name");
				String depart = stuRs.getString("department");
				System.out.println(id + " ,  " + name + " , " + depart);

				// id와 name을 리스트에 저장
				userList.add(new User(name, id));
			}

			stuRs.close();

		} catch (ClassNotFoundException error) {
			System.out.println("MySQL 드라이버 미설치 또는 드라이버 이름 오류");
		} catch (SQLException error) {
			System.out.println("DB 접속 오류");
			System.out.println("오류 코드: " + error.getErrorCode());
			System.out.println("오류 메시지: " + error.getMessage());
			error.printStackTrace(); // 스택 트레이스 출력
			// 로그 파일 확인하는 코드 추가 가능
		}

		Scanner input = new Scanner(System.in);
		boolean validUser = false;

		while (!validUser) {
			System.out.print("이름을 입력하세요: ");
			String userName = input.next();

			System.out.print("학번을 입력하세요: ");
			String userNumber = input.next();

			// 사용자 검증
			for (User user : userList) {
				if (user.getName().equals(userName) && user.getNumber().equals(userNumber)) {
					validUser = true;
					mUser = user;
					break;
				}
			}

			if (!validUser) {
				System.out.println("유저가 존재하지 않습니다. 다시 입력해주세요.");
			}
		}

		String greeting = "Ewha Course Evaluation";
		String tagline = "Welcome to ECE!";

		boolean quit = false;

		while (!quit) {
			System.out.println("***********************************************");
			System.out.println("\t" + greeting);
			System.out.println("\t" + tagline);

			menuIntroduction();

			System.out.print("메뉴 번호를 선택해주세요: ");
			int n = input.nextInt();

			if (n < 1 || n > 6) {
				System.out.println("1부터 6까지의 숫자를 입력하세요.");
			} else {
				if (mUser != null) {
					switch (n) {
						case 1:
							insertEIE();
							break;
						case 2:
							searchECE();
							break;
						case 3:
							modifyECE();
							break;
						case 4:
							// 강의평 삭제 함수
							deleteECE();
							break;
						case 5:
							// System.out.println("현재 고객 정보 : ");
							// System.out.println("이름 " + userName + " 연락처 " + userMobile);
							menuGuestInfo(mUser.getName(), mUser.getNumber());
							break;
						case 6:
							// 종료
							menuExit();
							quit = true;
							break;
					}
				} else {
					System.out.println("유저가 존재하지 않습니다.");
				}
			}
		}

	}

	public static void menuIntroduction() {
		System.out.println("******************************");
		System.out.println(" 1. 강의평가 입력하기 ");
		System.out.println(" 2. 강의평가 검색하기 ");
		System.out.println(" 3. 강의평가 수정하기 ");
		System.out.println(" 4. 강의평가 삭제하기 ");
		System.out.println(" 5. 회원정보 ");
		System.out.println(" 6. 로그아웃 ");
		System.out.println("******************************");
	}

	public static void menuGuestInfo(String name, String mobile) {
		System.out.println("현재 고객 정보 : ");
		System.out.println("이름 " + mUser.getName() + "   학번 " + mUser.getNumber());
	}

	public static void searchECE() {
		System.out.println("1. 강의명 및 교수명으로 검색하기");
		System.out.println("2. 강의내용으로 검색하기");
		System.out.println("3. 강의명 및 교수명으로 평균 강의평 검색하기");

		Scanner input = new Scanner(System.in);
		System.out.print("검색할 번호를 입력하세요: ");
		int searchOption = input.nextInt();

		switch (searchOption) {
			case 1:
				searchCourse();
				break;
			case 2:
				searchContents();
				break;
			case 3:
				searchAverageRating();
				break;
			default:
				System.out.println("유효하지 않은 옵션입니다.");
		}
	}
	public static void searchCourse() {
		Scanner input = new Scanner(System.in);
		System.out.print("검색할 강의명을 입력하세요: ");
		String courseName = input.nextLine();
		System.out.print("검색할 교수님 성함을 입력하세요: ");
		String professorName = input.nextLine();
		System.out.println("----------------------------");
	
		try {
			String createViewSql = "CREATE VIEW V AS " +
					"SELECT c.title, c.id AS course_id, p.name AS professor_name, r.student_id, r.contents, r.point " +
					"FROM rating r " +
					"JOIN course c ON r.course_id = c.id " +
					"JOIN professor p ON c.prof_id = p.id " +
					"WHERE c.title LIKE '%" + courseName + "%' AND p.name LIKE '%" + professorName + "%'";
	
			stmt.executeUpdate(createViewSql);
	
			String selectSql = "SELECT * FROM V";
			ResultSet result = stmt.executeQuery(selectSql);
	
			while (result.next()) {
				String title = result.getString("title");
				String courseId = result.getString("course_id");
				professorName = result.getString("professor_name");
				String studentId = result.getString("student_id");
				String contents = result.getString("contents");
				double ratingPoint = result.getDouble("point");
				System.out.println("강의 ID: " + courseId);
				System.out.println("강의명: " + title);
				System.out.println("교수님 이름: " + professorName);
				System.out.println("학생 ID: " + studentId);
				System.out.println("평점: " + ratingPoint);
				System.out.println("강의평 내용: " + contents);
				
				System.out.println("----------------------------");
			}
	
			result.close();
	
			String dropViewSql = "DROP VIEW V";
			stmt.executeUpdate(dropViewSql);
		} catch (SQLException e) {
			System.out.println("검색 중 오류 발생: " + e.getMessage());
		}
	}
	
	public static void searchContents() {
		Scanner input = new Scanner(System.in);
		System.out.print("강의내용을 입력하세요: ");
		String courseContents = input.nextLine();
		System.out.println("----------------------------");
		try {
			String sql = "CREATE VIEW V AS " +
					"SELECT c.title, c.id AS course_id, p.name AS professor_name, r.contents " +
					"FROM rating r " +
					"JOIN course c ON r.course_id = c.id " +
					"JOIN professor p ON c.prof_id = p.id " +
					"WHERE r.contents LIKE '%" + courseContents + "%'";
	
			stmt.executeUpdate(sql);
	
			sql = "SELECT * FROM V";
			ResultSet result = stmt.executeQuery(sql);
	
			while (result.next()) {
				String title = result.getString("title");
				String courseId = result.getString("course_id");
				String professorName = result.getString("professor_name");
				String contents = result.getString("contents");
				System.out.println("강의 ID: " + courseId);
				System.out.println("강의명: " + title);
				System.out.println("교수님 이름: " + professorName);
				System.out.println("강의평 내용: " + contents);
				System.out.println("----------------------------");
			}
	
			result.close();
	
			sql = "DROP VIEW V";
			stmt.executeUpdate(sql);
	
		} catch (SQLException e) {
			System.out.println("검색 중 오류 발생: " + e.getMessage());
		}
	}
	
	public static void searchAverageRating() {
		Scanner input = new Scanner(System.in);
		System.out.print("교수명을 입력하세요: ");
		String professorName = input.nextLine();
		System.out.print("과목명을 입력하세요: ");
		String courseTitle = input.nextLine();
		System.out.println("----------------------------");
	
		try {
			String sql = "SELECT AVG(point) AS average_rating " +
						 "FROM rating r " +
						 "JOIN course c ON r.course_id = c.id " +
						 "JOIN professor p ON c.prof_id = p.id " +
						 "WHERE p.name = '" + professorName + "' AND c.title = '" + courseTitle + "'";
	
			ResultSet result = stmt.executeQuery(sql);
	
			if (result.next()) {
				double averageRating = result.getDouble("average_rating");
				// System.out.println("교수명: " + professorName);
				// System.out.println("과목명: " + courseTitle);
				System.out.println("평균 강의평: " + averageRating);
			} else {
				System.out.println("일치하는 강의평이 없습니다.");
			}
	
			result.close();
	
		} catch (SQLException e) {
			System.out.println("검색 중 오류 발생: " + e.getMessage());
		}
	}
	
	public static void modifyECE() {
		if (mUser != null) {
			String studentId = mUser.getNumber();
			String studentName = mUser.getName();
			
			System.out.println("\n다음은 "+ studentName + "님이 남기신 강의평 목록입니다.\n");

			try {
				String selectSql = "SELECT r.rating_id, r.contents, r.point, c.title " +
						"FROM rating r " +
						"JOIN course c ON r.course_id = c.id " +
						"WHERE r.student_id = '" + studentId + "'";

				ResultSet result = stmt.executeQuery(selectSql);

				boolean hasRating = false;

				while (result.next()) {
					hasRating = true;
					int ratingId = result.getInt("rating_id");
					String contents = result.getString("contents");
					double point = result.getDouble("point");
					String courseTitle = result.getString("title");

					System.out.println("강의평 ID: " + ratingId);
					System.out.println("강의 제목: " + courseTitle);
					System.out.println("강의평 내용: " + contents);
					System.out.println("평점: " + point);
					System.out.println("-----------------------------");
				}

				if (!hasRating) {
					System.out.println("강의평가가 없습니다.");
					return;
				}

				System.out.println("강의평을 수정할 ID와 새로운 강의평 내용, 평점을 입력하세요.");

				Scanner input = new Scanner(System.in);
				System.out.print("강의평 ID: ");
				int ratingId = input.nextInt();
				input.nextLine(); // 개행 문자 제거

				System.out.print("새로운 강의평 내용: ");
				String newContents = input.nextLine();

				System.out.print("새로운 평점: ");
				double newPoint = input.nextDouble();

				if (newPoint < 1 || newPoint > 5) {
					System.out.println("강의평점은 1점에서 5점 사이만 입력할 수 있습니다.");
					return;
				}

				String updateSql = "UPDATE rating " +
						"SET contents = '" + newContents + "', point = " + newPoint +
						" WHERE rating_id = " + ratingId + " AND student_id = '" + studentId + "'";

				int rowsAffected = stmt.executeUpdate(updateSql);

				if (rowsAffected > 0) {
					System.out.println("강의평 수정이 완료되었습니다.");
				} else {
					System.out.println("일치하는 강의평 ID가 없습니다.");
				}

			} catch (SQLException e) {
				System.out.println("강의평 수정 중 오류 발생: " + e.getMessage());
			}
		} else {
			System.out.println("유저가 존재하지 않습니다.");
		}
	}
	public static void insertEIE() {
		try {
			// Display the course list
			System.out.println("<< 강의 목록 >>");
			String sql = "SELECT c.id AS course_id, c.title AS course_title, p.name AS professor_name " +
					"FROM course c " +
					"JOIN professor p ON c.prof_id = p.id";
			ResultSet courseRs = stmt.executeQuery(sql);
	
			while (courseRs.next()) {
				String courseId = courseRs.getString("course_id");
				String courseTitle = courseRs.getString("course_title");
				String professorName = courseRs.getString("professor_name");
				System.out.println("강의 ID: " + courseId + ", 강의명: " + courseTitle + ", 교수: " + professorName);
			}
	
			courseRs.close();
	
			// Prompt for the course ID
			Scanner input = new Scanner(System.in);
			System.out.print("평가하고 싶은 강의 ID를 입력하세요: ");
			String courseId = input.nextLine();
	
			System.out.print("강의 평가 점수를 입력하세요: ");
			double point = input.nextDouble();
			input.nextLine(); // Handle newline character
	
			System.out.print("강의평을 입력하세요: ");
			String contents = input.nextLine();
			contents = contents.replace("'", "''");
	
			sql = "INSERT INTO rating (rating_id, course_id, prof_id, student_id, point, contents) " +
					"SELECT MAX(rating_id) + 1, '" + courseId + "', c.prof_id, s.id, " + point + ", '" + contents + "' " +
					"FROM course c, student s, rating r " +
					"WHERE s.name = '" + mUser.getName() + "' AND s.id = " + mUser.getNumber() +
					" AND c.id = '" + courseId + "'";
			stmt.executeUpdate(sql);
	
			System.out.println("\n강의평이 성공적으로 추가되었습니다.");
	
		} catch (SQLException e) {
			System.out.println("강의평 추가 중 오류 발생: " + e.getMessage());
		}
	}
	
	
	public static void deleteECE() {
		if (mUser != null) {
			String studentId = mUser.getNumber();
			String studentName = mUser.getName();
			try {
				// Display the ratings for the current user
				String selectSql = "SELECT r.rating_id, c.title AS course_title, p.name AS professor_name, r.point, r.contents " +
						"FROM rating r " +
						"JOIN course c ON r.course_id = c.id " +
						"JOIN professor p ON c.prof_id = p.id " +
						"WHERE r.student_id = '" + studentId + "'";
	
				ResultSet result = stmt.executeQuery(selectSql);
	
				System.out.println("\n다음은 " + studentName + "님이 남기신 강의평 목록입니다.\n");
				boolean hasRating = false;
	
				while (result.next()) {
					hasRating = true;
					int ratingId = result.getInt("rating_id");
					String courseTitle = result.getString("course_title");
					String professorName = result.getString("professor_name");
					double point = result.getDouble("point");
					String contents = result.getString("contents");
	
					System.out.println("강의평 ID: " + ratingId);
					System.out.println("강의명: " + courseTitle);
					System.out.println("교수 이름: " + professorName);
					System.out.println("평점: " + point);
					System.out.println("강의평 내용: " + contents);
					System.out.println("-----------------------------");
				}
	
				if (!hasRating) {
					System.out.println("강의평가가 없습니다.");
					return;
				}
	
				// Prompt for the rating ID to delete
				Scanner input = new Scanner(System.in);
				System.out.print("삭제할 강의평 ID를 입력하세요: ");
				int ratingId = input.nextInt();
	
				// Check if the rating belongs to the current user
				String checkSql = "SELECT * FROM rating " +
						"WHERE rating_id = " + ratingId + " AND student_id = '" + studentId + "'";
				ResultSet checkResult = stmt.executeQuery(checkSql);
	
				if (checkResult.next()) {
					// Delete the rating
					String deleteSql = "DELETE FROM rating WHERE rating_id = " + ratingId;
					stmt.executeUpdate(deleteSql);
					System.out.println("강의평 삭제가 완료되었습니다.");
				} else {
					System.out.println("일치하는 강의평 ID가 없거나 권한이 없습니다.");
				}
	
				checkResult.close();
			} catch (SQLException e) {
				System.out.println("강의평 삭제 중 오류 발생: " + e.getMessage());
			}
		} else {
			System.out.println("유저가 존재하지 않습니다.");
		}
	}
	
	



	public static void menuExit() {
		System.out.println("로그아웃 되었습니다.");
		try {
			if (stuRs != null) {
				stuRs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
			System.out.println("MySQL 연결 해제 성공");
		} catch (SQLException e) {
			System.out.println("MySQL 연결 해제 실패: " + e.getMessage());
		}
	}

}
