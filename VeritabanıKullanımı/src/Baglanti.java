import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.Driver;

public class Baglanti {
// Mysql veritabanına bağlanmamız için bizim bir kullanıcı adı ve şifreye ihtiyacımız var
// Herhangi bir işlem yapmadığımız için bunlar otomatik olarak kullanıcı adı : root ve şifre boş oluyor
	
	//Veritabanına erişmek için gerekli olan bilgileri özellik olarak ekliyorum
	
	private String kullaniciAdi = "root";
	
	private String parola = "";
	
	// Hangi veritabanına bağlanmak istediğimizi söylememiz gerekiyor
	
	private String db_ismi = "demo" ;
	
	// Bağlanacağım veri tabanının hangi sunucuda olduğunu belirtiyorum
	// Eğer uzakta bir sunucu ise direkt olarak adresini vermem gerekiyor
	// Ama bizim şuanki veritabanımız yerel bir sunucuda olduğu için localhost değerini vermem gerekiyor
	
	private String host = "localhost";
	
	// Son olarak mysql veri tabanım belirli bir port ile çalışıyorsa bunuda belirtmem gerekiyor ekliyorum o yüzden
	
	private int port = 3306; 
	
	// Connection sınıfı üzerinden bir obje oluşturarak direkt olarak veritabanı bağlantımızı gerçekleştireceğiz
	
	private Connection con = null ; 

	// Statement class'ı sql sorgularını çalıştırmak için gerekli olan bir class'ımız
	// İçerisindeki metotları kullanarak sql sorgularımızı çalıştırıyoruz
	private java.sql.Statement  statement = null;
	
	
	// Statement'a göre daha esnek ve güvenilir bir yapısı olan PreparedStatament
	
	private PreparedStatement preparedStatement = null ;
	
	public void preparedCalisanlariGetir(int id ) {
		String sorgu = "Select * from calisanlar where id > ? and ad like ? ";
		// Bu soru işareti şu anlama geliyor
		// Sen belirttiğim sütunun o parametresinde yer tut ben sana daha sonra preparedStatement set metodları ile değer ekliyeceğim
		try {
			preparedStatement = con.prepareStatement(sorgu);
			preparedStatement.setInt(1, id);// 1. soru işaretinin yerine id değerini koy
			// Metoddaki ilk parametre değeri kaçıncı soru işareti yerine değer yazacağımızı belirtiyor
			preparedStatement.setString(2, "A%");
			
			ResultSet rs =  preparedStatement.executeQuery();
			while (rs.next()) {
				String ad = rs.getString("ad");
				String soyad= rs.getString("soyad");
				String email = rs.getString("email");
				
			System.out.println(" Ad : " + ad + " Soyad : " + soyad + " Email : " +email);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Verileri çekmek için bir metod oluşturalım
	public void calisanlariGetir() {
		
	// İlk olarak bir sql sorgusu oluşturmamız gerekiyor
		
		String sorgu = "Select * From calisanlar";
		// SQL sorgumda eğer bir koşul eklemek istiyorsam where komutunu kullanıyorum
		// Örn ; Id'si 2'den büyük olan gibi
		
	// Connection referansımız ile bir  statement objesi oluşturup bunu statement referansına atıyorum
		 try {
		statement = con.createStatement();
		ResultSet rs = 	statement.executeQuery(sorgu);
		// executeQuery metodu ile parametre olarak gönderdiğim sorguyu direkt olarak çalıştıracağım
		// Ayrıca bu metod bir ResultSet objesi return ediyor
		
		// Result set Select ile yapılan sorgularda tablodaki verileri satır satır okumamızı sağlıyor
		while (rs.next()) {
			// next metodu ile bir iterator gibi satır satır gidiyor
			// veri varsa true yoksa false değer dönüyor
			
			// Verileri alıyoruz
			int id = rs.getInt("id");  // kolon ismini girip getInt metodu ile o kolondaki veriyi getiriyorum
			String ad = rs.getString("ad");
			String soyad= rs.getString("soyad");
			String email = rs.getString("email");
			
		System.out.println("Id : " + id + " Ad : " + ad + " Soyad : " + soyad + " Email : " +email);	
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void calisanEkle() {
		try {
			statement = con.createStatement();// Veritabanı için bir sorgu yazıcaksak statement objesi ile
											   // yapıyoruz bu işlemleri
		String ad = "Alper";
		String soyad = "Güler";
		String email = "güleralper58@gmail.com";
		
		
String sorgu = "INSERT INTO calisanlar (ad,soyad,email) VALUES(" +"'"+ad+"',"+"'"+soyad+"',"+"'"+email+"')";
		// Her güncelleme işlemi için biz statement objesinin sahip oldupu executeUpdate metodunu kullanıyoruz
		// Bu güncelleme işlemi bir veri ekleme , veri silme , verileri güncelleme işlemleride olabilir
		statement.executeUpdate(sorgu);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void calisanGuncelle() {
		try {
			// Condition objesi ile bir statament objesi oluşturup statement referansına atıyorum
			statement = con.createStatement();
			
String sorgu = "UPDATE calisanlar SET  email = 'galatasaray@gmail.com' WHERE id > 3";
			// Statement objemin bu metodu ile tablodaki verileri güncelliyorum 
			statement.executeUpdate(sorgu);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void calisanSil() {
		try {
			statement = con.createStatement();
			// Tablodan kayıt silmek istiyorsam DELETE komutunu kullanıyorum
			// Eğer bir koşul kullanmazsam update komutundaki gibi tüm değerler etkilenecek
			// Yani tablodaki tüm veriler silinecek , bu yüzden where komutunu kullanmayı unutmamak gerekiyor
			
			String sorgu = "Delete from calisanlar where id > 5";
			statement.executeUpdate(sorgu);
			// executeUpdate metodu int bir değer dönüyor bu değer bizim yaptığımız ekleme , güncelleme , silme
			// işlemlerinden sonra kaç satırın bu işlemlerden etkilendiğinin sayısını döndürüyor bize
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Baglanti() {
		 
	// Bir tane url vermem gerekiyor , bu url'leride buradaki özellikler ile oluşturacağız
	
	String url = "jdbc:mysql://" + host + ":" + port +  "/" + db_ismi + "?useUnicode=true&characterEncoding=utf8" ;
    //"jdbc:mysql://localhost:3306/demo" şeklinde bir bağlantı gerçekleştirmiş oluyoruz aslında
	
	//Driverı başlatıyorum, bunu yapmazsak bazen sıkıntı çıkabilir
	
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		// Böyle yaparak JDBC Driver'ını ekstradan özellikle çağırmış oluyoruz burada
	} catch (ClassNotFoundException e) {
		System.out.println("Driver bulunamadı");
		e.printStackTrace();
	}
	
	try {
		con = DriverManager.getConnection(url, kullaniciAdi, parola);
		System.out.println("Bağlantı Başarılı :)");
	} catch (SQLException e) {
		System.out.println("Veritabanına bağlanılamadı!!!");
		e.printStackTrace();
	}
	
	
	}
	
	
	public static void main(String[] args) {
		
		Baglanti baglanti = new Baglanti();
//		System.out.println("Veriler güncellenmeden önce\n");
//		baglanti.calisanlariGetir();
//		
//		System.out.println("\n Veriler güncellendikten sonra\n");
		// baglanti.calisanGuncelle();
		// baglanti.calisanSil();
		// baglanti.calisanEkle();
		baglanti.preparedCalisanlariGetir(3);
		//baglanti.calisanlariGetir();
		
	}
	
	
	
	// Tabloya veri eklemek için : 
	// Insert into tablo_adı kolon isimleri ('','') değerler ('','')
	
	// Tablodaki verileri güncellemek için
	// Update tablo_adı Set kolon ismi = değer where koşulumuz
	// Eğer bir koşul belirtmez isek tüm değerler etkilenir tablodaki
	
	// Tablodaki verileri silmek için 
	// Delete from  tablo_ismi  where koşulumuz
	// Eğer bir koşul belirtmezsek tablodaki tüm veriler silinecek
	
	// Tablodan bütün verileri çekmek için : 
	// Select * from tablo_ismi  ,  * tablodaki tüm sütunları temsil ediyor oradaki tüm verileri getir 
	// Tablodan belirli sütundaki verileri çekmek için :
	// Select kolon_isimleri from tablo_ismi
	
}
