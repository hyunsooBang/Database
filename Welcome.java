import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// 유저 클래스
class User {
	private String name;
	private String number;

	// 유저 생성
	public User(String name, String number) {
		this.name = name; // 유저 이름
		this.number = number; // 유저 학번
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}

public class Welcome {

	static User mUser;  // 현재 로그인한 유저
	static List<User> userList = new ArrayList<>();  // 유저 리스트
	static ResultSet stuRs;  // 학생 정보 결과셋
	static Statement stmt;  // SQL 문 실행을 위한 Statement 객체
	static Connection conn;  // 데이터베이스 연결 객체

	public static void main(String[] args) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// MySQL 데이터베이스에 연결

			//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/project", "root", "1234");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root", "tjdwns246246");
			
			
			System.out.println("MySQL DB 연결 성공");

			// SQL 연결
			stmt = conn.createStatement();

			String sql = "select* from student";
			stuRs = stmt.executeQuery(sql);

			while (stuRs.next()) {
				String id = stuRs.getString("id");
				String name = stuRs.getString("name");
	

				// 데이터베이스에 저장된 student id와 name을 유저 리스트에 저장
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
		}

		Scanner input = new Scanner(System.in);
		boolean validUser = false;

		while (!validUser) {
			System.out.print("이름을 입력하세요: ");
			String userName = input.nextLine();

			System.out.print("학번을 입력하세요: ");
			String userNumber = input.nextLine();

			// 사용자 검증
			// 입력받은 유저 이름 및 학번이 유저리스트 내에 존재하는지 확인
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
			System.out.println("\tLogged in as: " + mUser.getName());

			menuIntroduction();

			System.out.print("메뉴 번호를 선택해주세요: ");
			int n = input.nextInt();

			if (n < 1 || n > 6) {
				System.out.println("1부터 5까지의 숫자를 입력하세요.");
			} else {
				if (mUser != null) {
					switch (n) {
						case 1:
							// 강의평 삽입 함수
							insertECE();
							break;
						case 2:
							// 강의평 검색 함수
							searchECE();
							break;
						case 3:
							// 강의평 수정 함수
							modifyECE();
							break;
						case 4:
							// 강의평 삭제 함수
							deleteECE();
							break;
						case 5:
							// 유저 정보 출력 함수
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

	// 메뉴 설명 함수
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

	// 현재 유저 정보 출력하는 함수
	public static void menuGuestInfo(String name, String mobile) {
		System.out.println("현재 고객 정보 : ");
		System.out.println("이름 " + mUser.getName() + "   학번 " + mUser.getNumber());
	}

	// 강의평가 검색하는 함수
	public static void searchECE() {
		System.out.println("1. 강의명 및 교수명으로 검색하기");
		System.out.println("2. 강의내용으로 검색하기");
		System.out.println("3. 강의명 및 교수명으로 평균 강의평 검색하기");

		Scanner input = new Scanner(System.in);
		System.out.print("검색할 번호를 입력하세요: ");
		int searchOption = input.nextInt();

		switch (searchOption) {
			case 1:
				searchCourse(); // 강의명 및 교수명으로 검색 메서드 호출
				break;
			case 2:
				searchContents(); // 강의내용으로 검색 메서드 호출
				break;
			case 3:
				searchAverageRating(); // 강의명 및 교수명으로 평균 강의평 검색 메서드 호출
				break;
			default:
				System.out.println("유효하지 않은 옵션입니다.");
		}
	}

	// 강의평 이름과 교수명으로 강의평 검색하는 함수
	public static void searchCourse() {
		// 검색할 강의명과 교수명을 입력 받음
		Scanner input = new Scanner(System.in);
		System.out.print("검색할 강의명을 입력하세요: ");
		String courseName = input.nextLine();
		System.out.print("검색할 교수님 성함을 입력하세요: ");
		String professorName = input.nextLine();
		System.out.println("----------------------------");

		try {
			// 임시로 뷰를 생성하여 검색 조건에 해당하는 데이터를 조회
			String createViewSql = "CREATE VIEW V AS " +
					"SELECT c.title, c.id AS course_id, p.name AS professor_name, r.student_id, r.contents, r.point " +
					"FROM rating r " +
					"JOIN course c ON r.course_id = c.id " +
					"JOIN professor p ON c.prof_id = p.id " +
					"WHERE c.title LIKE '%" + courseName + "%' AND p.name LIKE '%" + professorName + "%'";

			stmt.executeUpdate(createViewSql);
			
			// 뷰에서 데이터를 선택하여 출력
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
				System.out.println("교수 이름: " + professorName);
				System.out.println("학생 ID: " + studentId);
				System.out.println("평점: " + ratingPoint);
				System.out.println("강의평 내용: " + contents);

				System.out.println("----------------------------");
			}

			result.close();
			// 생성한 뷰 삭제
			String dropViewSql = "DROP VIEW V";
			stmt.executeUpdate(dropViewSql);
		} catch (SQLException e) {
			System.out.println("검색 중 오류 발생: " + e.getMessage());
		}
	}

	// 강의평 내용으로 강의평 검색하는 함수
	public static void searchContents() {
		// 검색할 강의 내용을 입력 받음
		Scanner input = new Scanner(System.in);
		System.out.print("강의내용을 입력하세요: ");
		String courseContents = input.nextLine();
		System.out.println("----------------------------");
		try {

			// 기존에 생성된 'V' 뷰가 있으면 삭제
			String dropViewSql = "DROP VIEW IF EXISTS V";
			stmt.executeUpdate(dropViewSql);

			// 임시로 뷰를 생성하여 검색 조건에 해당하는 데이터를 조회
			// rating, course, professor 테이블을 조인하고, r.contents 필드에서 courseContents 변수에 포함된
			// 내용을 포함하는 레코드를 선택하여 임시 뷰(V)를 생성
			
			String sql = "CREATE VIEW V AS " +
					"SELECT c.title, c.id AS course_id, p.name AS professor_name, r.contents, r.point, r.student_id " +
					"FROM rating r " +
					"JOIN course c ON r.course_id = c.id " +
					"JOIN professor p ON c.prof_id = p.id " +
					"WHERE r.contents LIKE '%" + courseContents + "%'";

			stmt.executeUpdate(sql);
			// 뷰에서 데이터를 선택하여 출력
			sql = "SELECT * FROM V";
			ResultSet result = stmt.executeQuery(sql);

			while (result.next()) {
				String title = result.getString("title");
				String courseId = result.getString("course_id");
				String professorName = result.getString("professor_name");
				String contents = result.getString("contents");
				String studentId = result.getString("student_id");
				double ratingPoint = result.getDouble("point");

				System.out.println("강의 ID: " + courseId);
				System.out.println("강의명: " + title);
				System.out.println("교수 이름: " + professorName);

				System.out.println("학생 ID: " + studentId);
				System.out.println("평점: " + ratingPoint);
				System.out.println("강의평 내용: " + contents);
				System.out.println("----------------------------");
			}

			result.close();
			// 생성한 뷰 삭제
			sql = "DROP VIEW V";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println("검색 중 오류 발생: " + e.getMessage());
		}
	}

	// 특정 course의 평균 강의평점을 검색하는 함수
	public static void searchAverageRating() {
		Scanner input = new Scanner(System.in);

		System.out.print("과목명을 입력하세요: ");
		String courseTitle = input.nextLine();
		System.out.print("교수명을 입력하세요: ");
		String professorName = input.nextLine();
		System.out.println("----------------------------");

		// rating, course, professor 테이블을 조인하고, 특정 교수 이름과 강의 제목에 해당하는 레코드들의 평균 평점을 조회
		try {
			String sql = "SELECT AVG(point) AS average_rating " +
            "FROM rating r " +
            "JOIN course c ON r.course_id = c.id " +
            "JOIN professor p ON c.prof_id = p.id " +
            "WHERE p.name = '" + professorName + "' AND c.title = '" + courseTitle + "'" +
            "GROUP BY c.title, p.name";

			ResultSet result = stmt.executeQuery(sql);

			if (result.next()) {
				double averageRating = result.getDouble("average_rating");
				System.out.printf("평균 강의평점: %.2f%n", averageRating);
			} else {
				System.out.println("일치하는 강의평이 없습니다.");
			}

			result.close();

		} catch (SQLException e) {
			System.out.println("검색 중 오류 발생: " + e.getMessage());
		}
	}

	// 자신이 작성한 강의평 수정하는 함수
	public static void modifyECE() {
		if (mUser != null) {
			String studentId = mUser.getNumber();
			String studentName = mUser.getName();

			System.out.println("\n다음은 " + studentName + "님이 남기신 강의평 목록입니다.\n");
			// studentId에 해당하는 학생의 강의평을 조회
			try {
				String selectSql = "SELECT r.rating_id, r.contents, r.point, c.title, p.name AS professor_name " +
						"FROM rating r " +
						"JOIN course c ON r.course_id = c.id " +
						"JOIN professor p ON c.prof_id = p.id " +
						"WHERE r.student_id = '" + studentId + "'";

				ResultSet result = stmt.executeQuery(selectSql);

				// 강의평가가 없는 경우를 대비해 불리언 변수 생성
				boolean hasRating = false;
				// 강의평가가 있는 경우 결과 출력
				while (result.next()) {
					hasRating = true;
					int ratingId = result.getInt("rating_id");
					String contents = result.getString("contents");
					double point = result.getDouble("point");
					String courseTitle = result.getString("title");
					String professorName = result.getString("professor_name");

					System.out.println("강의평 ID: " + ratingId);
					System.out.println("강의명: " + courseTitle);
					System.out.println("교수 이름: " + professorName);
					System.out.println("평점: " + point);
					System.out.println("강의평 내용: " + contents);
					System.out.println("-----------------------------");
				}
				// 강의평가가 없는 경우
				if (!hasRating) {
					System.out.println("강의평가가 없습니다.");
					return;
				}

				System.out.println("강의평을 수정할 ID와 새로운 강의평 내용, 평점을 입력하세요.");

				Scanner input = new Scanner(System.in);
				System.out.print("강의평 ID: ");
				int ratingId = input.nextInt();
				input.nextLine(); // 개행 문자 제거

				System.out.print("새로운 평점: ");
				double newPoint = input.nextDouble();

				// 강의평점은 1-5점만 입력 가능
				if (newPoint < 1 || newPoint > 5) {
					System.out.println("강의평점은 1점에서 5점 사이만 입력할 수 있습니다.");
					return;
				}
				input.nextLine();
				System.out.print("새로운 강의평 내용: ");
				
				String newContents = input.nextLine();
				
				// 유저에게 입력받은 새로운 평점과 내용으로 강의평 업데이트
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

	// 새로운 강의평 추가 함수
	public static void insertECE() {
		try {
			// 전체 강의목록 출력
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

			// 강의 ID, 평점, 내용을 입력받기
			Scanner input = new Scanner(System.in);
			System.out.print("평가하고 싶은 강의 ID를 입력하세요: ");
			String courseId = input.nextLine();

			System.out.print("강의 평점을 입력하세요: ");
			double point = input.nextDouble();
			input.nextLine(); // Handle newline character

			System.out.print("강의평 내용을 입력하세요: ");
			String contents = input.nextLine();
			contents = contents.replace("'", "''");

			// 유저 정보 기반으로 입력받은 강의 ID, 평점, 내용을 rating 테이블에 삽입
			
			sql = "INSERT INTO rating (course_id, prof_id, student_id, point, contents) " +
			"SELECT '" + courseId + "', c.prof_id, s.id, " + point + ", '" + contents + "' " +
			"FROM course c, student s " +
			"WHERE s.name = '" + mUser.getName() + "' AND s.id = " + mUser.getNumber() +
			" AND c.id = '" + courseId + "'";

			stmt.executeUpdate(sql);

			System.out.println("\n강의평이 성공적으로 추가되었습니다.");

		} catch (SQLException e) {
			System.out.println("강의평 추가 중 오류 발생: " + e.getMessage());
		}
	}

	// 기존 강의평 삭제 함수
	public static void deleteECE() {
		if (mUser != null) {
			String studentId = mUser.getNumber();
			String studentName = mUser.getName();
			try {
				// 작성한 전체 강의평 보여주기
				String selectSql = "SELECT r.rating_id, c.title AS course_title, p.name AS professor_name, r.point, r.contents "+
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

				// 삭제할 강의평 ID 입력받기
				Scanner input = new Scanner(System.in);
				System.out.print("삭제할 강의평 ID를 입력하세요: ");
				int ratingId = input.nextInt();

				// 본인이 작성한 강의평인지 확인 과정
				String checkSql = "SELECT * FROM rating " +
						"WHERE rating_id = " + ratingId + " AND student_id = '" + studentId + "'";
				ResultSet checkResult = stmt.executeQuery(checkSql);

				if (checkResult.next()) {
					// rating 테이블에서 강의평 삭제하기
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

	// 로그아웃 함수 - mySQL 해제
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
