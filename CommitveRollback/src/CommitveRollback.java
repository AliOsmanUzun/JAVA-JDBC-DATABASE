
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommitveRollback {

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
    
    private Connection con = null;
    
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    
    /*Database Transaction
    
    Çoğu zaman programlarımızda bir çok veritabanı işlemini ard arda yaparız.
    Örneğin elimizde birbiriyle bağlantılı 3 tane tablo güncelleme işlemimiz var(delete,update). 
    Bu işlemleri ve sorguları ard arda çalıştırdığımızı düşünelim.
    
    statement.executeUpdate(sorgu1);
    statement.executeUpdate(sorgu2); // Burada herhangi bir hata oldu ve programımız sona erdi.
    statement.executeUpdate(sorgu3);
    
    
    Böyle bir durumda 2.sorgumuzda herhangi bir hata oluyor. 
    Ancak 1.sorguda hata olmadığı için bu sorgumuz veritabanımızı güncelledi.
    Ancak bu sorgular birbiriyle bağlantılı ise 1.sorgunun da çalışmaması gerekiyor.
    İşte biz böyle durumların önüne geçmek için Transactionları kullanıyoruz.
    
    (ATM Örneği)
    
    Transaction mantığını kullanmak için bu sorguların sadece hiçbir sorun oluşmadığında 
    toplu çalışmasını istiyoruz. 
    
    Java bu sorguları yazdığımız andan itibaren otomatik olarak sorguları sorgusuz sualsiz 
    çalıştırır. Onun için ilk olarak con.setAutoCommit(false) şeklinde bir şey yaparak bu durumu 
    engelliyoruz.
    
    commit() : Bütün sorguları çalıştır. Sorun olmadığı zaman hepsini çalıştırmak için kullanıyoruz.
    rollback(): Bütün sorguları iptal et. Sorun olduğu zaman hiçbirini çalıştırmamak için kullanıyoruz.
    
    
    Yani bu sefer programlarımızı biraz daha güvenli yazmış oluyoruz.
    
    Not : setAutoCommit(false) yazsak bile Veritabanını güncellemeyen bir sorgumuz varsa,
    herhangi bir güvenlik sıkıntı olmayacağından çalıştırılır.
    */
    
    public CommitveRollback() {
        
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

    public void commitverollback() {
    	Scanner scanner = new Scanner(System.in);
    	
    	
    // Java'ya sen bu sql komutlarını çalıştırma işini otomatik yapma ben manuel olarak kendim yapacağım demiş oluyorum
    try {
		con.setAutoCommit(false);
		// Bu komut ile sorgularımız biz onay vermeden çalışmayacak
		
		String sorgu1 = "Delete from calisanlar where id = 5";
		
		String sorgu2 = "Update calisanlar Set email = 'yilmazbaris@gmail.com' where id = 3";
		
		System.out.println("Güncellenmeden Önce");
		calisanlariGetir();
		// Güncelleme değil veri çekme işlemi yaptığı için autocommitimiz false olsa bile program tarafından çalıştırılacak
		
		statement = con.createStatement();
		
		statement.executeUpdate(sorgu1);
		
		statement.executeUpdate(sorgu2);
		
		System.out.print("İşlemleriniz kaydedilsin mi ?(yes/no) : ");
		String cevap = scanner.nextLine();
		if (cevap.equals("yes")) {
			con.commit(); // Sorguların hepsini beraber çalıştır
			calisanlariGetir();
			System.out.println("Veritabanı güncellendi....");
		}
		else {
			con.rollback();// Bütün sorguları iptal et
			System.out.println("Veritabanı güncellemesi iptal edildi");
			calisanlariGetir();
		}
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
		
	}
    public void calisanlariGetir() {
        
        String sorgu = "Select * From calisanlar";
      
        try {
            statement = con.createStatement();
            
            ResultSet rs = statement.executeQuery(sorgu);
            
            while (rs.next()) {
                
                int id = rs.getInt("id");
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                String email = rs.getString("email");
                
                System.out.println("Id : " + id + "Ad: " + ad + "Soyad : " + soyad + " Email : " + email);
                
                
            }
            
            
        } catch (SQLException ex) 	{
            Logger.getLogger(CommitveRollback.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    public static void main(String[] args) {
        CommitveRollback comroll = new CommitveRollback();
        // baglanti.calisanlariGetir();
        comroll.commitverollback();
        
     
        
        
    }
}
