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

			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/project", "root", "1234");
			System.out.println("MySQL DB 연결 성공");

			// SQL 연결
			stmt = conn.createStatement();

			String sql = "select* from student";
			stuRs = stmt.executeQuery(sql);

			while (stuRs.next()) {
				int id = stuRs.getInt("id");
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
			int userNumber = input.nextInt();

			// 사용자 검증
			for (User user : userList) {
				if (user.getName().equals(userName) && user.getNumber() == userNumber) {
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
							// 강의평 입력 함수
							// ex) insertEIE();
							break;
						case 2:
							searchECE();
							break;
						case 3:
							// 강의평 수정 함수
							// ex) modifyEIE();
							break;
						case 4:
							// 강의평 삭제 함수
							// ex) deleteEIE();
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

	public static void menuGuestInfo(String name, int mobile) {
		System.out.println("현재 고객 정보 : ");
		System.out.println("이름 " + mUser.getName() + "   학번 " + mUser.getNumber());
	}

	public static void searchECE() {
		System.out.println("1. 강의명으로 검색하기");
		System.out.println("2. 강의내용으로 검색하기");

		Scanner input = new Scanner(System.in);
		System.out.print("검색할 번호를 입력하세요: ");
		int searchOption = input.nextInt();

		switch (searchOption) {
			case 1:
				searchName();
				break;
			case 2:
				searchContents();
				break;
			default:
				System.out.println("유효하지 않은 옵션입니다.");
		}
	}

	public static void searchName() {
		// 강의명으로 데이터베이스에서 검색 수행
	}

	public static void searchContents() {
		Scanner input = new Scanner(System.in);
		System.out.print("강의내용을 입력하세요: ");
		String courseContents = input.nextLine();

		// 강의내용으로 데이터베이스에서 검색 수행
		// 결과를 출력하는 로직 추가
		// 예: SELECT * FROM rating WHERE contents LIKE '%courseContents%';
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