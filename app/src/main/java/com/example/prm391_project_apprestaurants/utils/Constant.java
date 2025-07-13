package com.example.prm391_project_apprestaurants.utils;

public class Constant {
    public static final String SQL_CREATE_TABLE_USERS = """
            CREATE TABLE IF NOT EXISTS Users (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE,
                Password TEXT NOT NULL,
                Email TEXT,
                Role TEXT DEFAULT 'User', -- 'User' hoặc 'Admin'
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                IsActive INTEGER DEFAULT 0
            );
            """;
    public static final String SQL_CREATE_TABLE_RESTAURANTS = """
            CREATE TABLE IF NOT EXISTS Restaurants (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT NOT NULL,                    -- Tên quán ăn
                Description TEXT,                      -- Mô tả ngắn về quán
                Address TEXT NOT NULL,                 -- Địa chỉ đầy đủ
                District TEXT,                         -- Quận/Huyện (Hoàng Mai, Cầu Giấy,...)
                PriceRange TEXT,                       -- Mức giá: 'Dưới 30k', '30-50k',...
                Category TEXT,                         -- Loại món: Cơm, Bún, Mì, Lẩu,...
                OpeningHours TEXT,                     -- Giờ mở cửa, ví dụ: "8:00 - 22:00"
                PhoneNumber TEXT,                      -- Số điện thoại liên hệ
                Website TEXT,                          -- Website nếu có
                ImageUrl TEXT,                         -- URL ảnh đại diện
                Latitude REAL,                         -- Tọa độ GPS (nếu dùng Google Maps)
                Longitude REAL,                        -- Tọa độ GPS
                IsHidden INTEGER DEFAULT 0,            -- 0: hiển thị, 1: bị ẩn (Admin kiểm soát)
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TEXT DEFAULT CURRENT_TIMESTAMP
            );
            """;
    public static final String SQL_CREATE_TABLE_FAVORITES = """
            CREATE TABLE IF NOT EXISTS Favorites (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                UserId INTEGER NOT NULL,
                RestaurantId INTEGER NOT NULL,
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (UserId) REFERENCES Users(Id),
                FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
            );
            """;
    public static final String SQL_CREATE_TABLE_REVIEWS = """
                CREATE TABLE IF NOT EXISTS Reviews (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    UserId INTEGER NOT NULL,
                    RestaurantId INTEGER NOT NULL,
                    Content TEXT NOT NULL,
                    Rating INTEGER NOT NULL DEFAULT 5,
                    CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (UserId) REFERENCES Users(Id),
                    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
                );
            """;

    public static final String SQL_CREATE_TABLE_MENUS = """
                CREATE TABLE IF NOT EXISTS Menus (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    RestaurantId INTEGER NOT NULL,
                    DishName TEXT NOT NULL,
                    Price TEXT NOT NULL,
                    ImageUrl TEXT,
                    Description TEXT,
                    CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    IsHidden INTEGER DEFAULT 0,            -- 0: hiển thị, 1: bị ẩn (Admin kiểm soát)
                    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
                );
            """;
    public static final String SQL_CREATE_TABLE_CATEGORIES = """
                CREATE TABLE IF NOT EXISTS Categories (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    Name TEXT NOT NULL UNIQUE
                );
            """;
    public static final String SQL_CREATE_TABLE_RESTAURANT_CATEGORY = """
                CREATE TABLE IF NOT EXISTS RestaurantCategory (
                    RestaurantId INTEGER NOT NULL,
                    CategoryId INTEGER NOT NULL,
                    PRIMARY KEY (RestaurantId, CategoryId),
                    FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id),
                    FOREIGN KEY (CategoryId) REFERENCES Categories(Id)
                );
            """;


    public static final String SQL_CREATE_TABLE_REVIEW_MEDIA = """
                CREATE TABLE IF NOT EXISTS ReviewMedia (
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ReviewId INTEGER NOT NULL,
                    MediaUrl TEXT NOT NULL,
                    MediaType TEXT DEFAULT 'image', -- image | video
                    CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (ReviewId) REFERENCES Reviews(Id)
                );
            """;


    public static final String SQL_INSERT_TABLE_USERS = """ 
            INSERT INTO Users (Username, Password, Email, Role,IsActive) VALUES
            ('admin', 'adminpass', 'admin@gmail.com', 'Admin',1),
            ('user1', 'userpass1', 'user1@gmail.com', 'User',1),
            ('user2', 'userpass2', 'user2@gmail.com', 'User',1),
            ('user3', 'userpass3', 'user3@gmail.com', 'User',0),
            ('user4', 'userpass4', 'user4@gmail.com', 'User',0),
            ('user5', 'userpass5', 'user5@gmail.com', 'User',0);
            
            """;
    public static final String SQL_INSERT_TABLE_RESTAURANTS = """
            INSERT INTO Restaurants\s
            (Name, Description, Address, District, PriceRange, Category, OpeningHours, PhoneNumber, Website, ImageUrl, Latitude, Longitude)
            VALUES
            ('Bún Chả Hương Liên', 'Nơi Tổng thống Obama từng ăn', '24 Lê Văn Hưu', 'Hai Bà Trưng', '30-50k', 'Bún', '9:00 - 21:00', '0241234567', 'https://huonglien.vn', 'https://i.imgur.com/buncha.jpg', 21.0156, 105.8529),
            ('Phở Thìn', 'Phở bò nổi tiếng hơn 30 năm', '13 Lò Đúc', 'Hai Bà Trưng', '50-100k', 'Phở', '6:00 - 21:00', '0247654321', 'https://phothin.vn', 'https://i.imgur.com/pho.jpg', 21.0143, 105.8601),
            ('Bánh Mì 25', 'Bánh mì ngon nổi tiếng với khách Tây', '25 Hàng Cá', 'Hoàn Kiếm', '30-50k', 'Bánh mì', '7:00 - 20:00', '0249988776', 'https://banhmi25.vn', 'https://i.imgur.com/banhmi.jpg', 21.0365, 105.8467),
            ('Chay Tâm An', 'Quán ăn chay yên tĩnh, thanh đạm', '11 Nguyễn Du', 'Hai Bà Trưng', '30-50k', 'Chay', '9:00 - 20:00', '0243344556', '', 'https://i.imgur.com/chay.jpg', 21.0162, 105.8538),
            ('Cơm Tấm Cali', 'Cơm tấm kiểu miền Nam giữa lòng Hà Nội', '5 Láng Hạ', 'Đống Đa', '50-100k', 'Cơm', '10:00 - 22:00', '0241112223', '', 'https://i.imgur.com/comtam.jpg', 21.0267, 105.8192),
            ('Bún Riêu Gánh', 'Bún riêu cua gánh truyền thống', '3 Phan Đình Phùng', 'Ba Đình', '30-50k', 'Bún', '6:30 - 19:00', '0248787878', '', 'https://i.imgur.com/bunrieu.jpg', 21.0389, 105.8411),
            ('Lẩu Đức Trọc', 'Lẩu đủ loại, nổi tiếng đông khách', '100 Yên Phụ', 'Tây Hồ', '100-200k', 'Lẩu', '10:00 - 23:00', '0244343434', 'https://lauductroc.vn', 'https://i.imgur.com/lau.jpg', 21.0501, 105.8432),
            ('Xôi Yến', 'Xôi xéo đậm đà, truyền thống Hà Nội', '35B Nguyễn Hữu Huân', 'Hoàn Kiếm', '30-50k', 'Xôi', '6:00 - 23:00', '0242323232', '', 'https://i.imgur.com/xoiyen.jpg', 21.0332, 105.8543),
            ('Mì Vằn Thắn Bình Tây', 'Mì sợi thủ công gia truyền', '12 Nguyễn Biểu', 'Ba Đình', '30-50k', 'Mì', '7:00 - 20:00', '0241122112', '', 'https://i.imgur.com/miwanthan.jpg', 21.0381, 105.8442),
            ('Cafe Giảng', 'Cafe trứng truyền thống từ 1946', '39 Nguyễn Hữu Huân', 'Hoàn Kiếm', '30-50k', 'Nước', '7:30 - 22:30', '0245566778', 'https://caffegiang.com', 'https://i.imgur.com/cafe.jpg', 21.0335, 105.8541);
            """;
    public static final String SQL_INSERT_TABLE_FAVORITES = """ 
            
            INSERT INTO Favorites (UserId, RestaurantId) VALUES
            (2, 1),
            (2, 2),
            (2, 3),
            (3, 3),
            (5, 4),
            (5, 5);
            """;
    public static final String SQL_INSERT_TABLE_REVIEWS = """
                INSERT INTO Reviews (UserId, RestaurantId, Content, Rating) VALUES
                (2, 1, 'Bún chả ngon, không gian sạch sẽ.', 5),
                (2, 2, 'Phở đậm đà, ăn rất vừa miệng.', 4),
                (3, 3, 'Bánh mì giòn, pate thơm.', 5),
                (3, 4, 'Không gian yên tĩnh, đồ ăn chay ngon.', 5),
                (4, 5, 'Cơm tấm ổn, nước mắm ngon.', 4),
                (5, 6, 'Bún riêu ngon nhưng hơi đông khách.', 3),
                (4, 7, 'Lẩu nhiều đồ, phục vụ nhanh.', 5),
                (3, 8, 'Xôi nhiều topping, ăn rất đã.', 4),
                (2, 9, 'Mì thủ công sợi mềm.', 5),
                (4, 10, 'Cafe trứng siêu ngon!', 5);
            """;

    public static final String SQL_INSERT_TABLE_MENUS = """
            INSERT INTO Menus (RestaurantId, DishName, Price, ImageUrl, Description) VALUES
            -- 1. Bún Chả Hương Liên
            (1, 'Bún chả truyền thống', '40k', '', 'Bún chả thịt nướng thơm ngon'),
            (1, 'Nem cua bể', '30k', '', 'Nem chiên giòn rụm, nhân cua hấp dẫn'),
            (1, 'Trà đá', '5k', '', 'Thức uống quen thuộc kèm bữa ăn'),
            
            -- 2. Phở Thìn
            (2, 'Phở bò tái lăn', '60k', '', 'Phở đặc trưng đậm đà hương vị'),
            (2, 'Phở bò chín', '55k', '', 'Phở bò chín thơm ngon, nước dùng trong'),
            (2, 'Sữa đậu nành', '10k', '', 'Đồ uống nhẹ nhàng sau bữa phở'),
            
            -- 3. Bánh Mì 25
            (3, 'Bánh mì pate trứng', '30k', '', 'Bánh mì giòn với pate nhà làm'),
            (3, 'Bánh mì thịt nướng', '35k', '', 'Thịt nướng đậm đà, rau sống tươi ngon'),
            (3, 'Sinh tố xoài', '25k', '', 'Thức uống mát lạnh trái cây tươi'),
            
            -- 4. Chay Tâm An
            (4, 'Cơm chay thập cẩm', '40k', '', 'Gồm rau, đậu hũ, chả chay'),
            (4, 'Bún Huế chay', '35k', '', 'Bún Huế phiên bản chay, thanh đạm'),
            (4, 'Trà sen', '15k', '', 'Trà thanh lọc cơ thể, vị dịu nhẹ'),
            
            -- 5. Cơm Tấm Cali
            (5, 'Cơm tấm sườn bì chả', '55k', '', 'Sườn nướng, chả trứng, bì trộn'),
            (5, 'Canh rong biển', '15k', '', 'Canh nhẹ dễ ăn kèm cơm'),
            (5, 'Nước sâm', '10k', '', 'Giải nhiệt ngày hè'),
            
            -- 6. Bún Riêu Gánh
            (6, 'Bún riêu cua', '40k', '', 'Nước dùng riêu cua thơm, đậm vị'),
            (6, 'Bún riêu tóp mỡ', '45k', '', 'Thêm tóp mỡ giòn, béo ngậy'),
            (6, 'Chè đậu xanh', '12k', '', 'Tráng miệng ngọt nhẹ'),
            
            -- 7. Lẩu Đức Trọc
            (7, 'Lẩu thái chua cay', '200k', '', 'Lẩu chua cay hấp dẫn đủ topping'),
            (7, 'Ba chỉ bò Mỹ', '150k', '', 'Thịt bò nhập khẩu thái lát mỏng'),
            (7, 'Nước ngọt các loại', '15k', '', 'Pepsi, Coca, 7Up...'),
            
            -- 8. Xôi Yến
            (8, 'Xôi xéo chả quế', '30k', '', 'Xôi mềm dẻo, chả quế thơm lừng'),
            (8, 'Xôi pate trứng', '28k', '', 'Phối hợp vị béo và mặn vừa phải'),
            (8, 'Sữa đậu', '10k', '', 'Đồ uống nhẹ nhàng buổi sáng'),
            
            -- 9. Mì Vằn Thắn Bình Tây
            (9, 'Mì vằn thắn nước', '35k', '', 'Nước dùng ngọt thanh, hoành thánh nhân thịt'),
            (9, 'Mì khô trộn', '37k', '', 'Mì khô với sốt đặc biệt'),
            (9, 'Trà đá', '5k', '', 'Thức uống phổ biến kèm bữa'),
            
            -- 10. Cafe Giảng
            (10, 'Cafe trứng', '35k', '', 'Cafe trứng đặc sản Hà Nội'),
            (10, 'Cafe sữa đá', '30k', '', 'Đậm đà vị cafe Việt'),
            (10, 'Cacao trứng nóng', '40k', '', 'Biến tấu độc đáo từ cacao và trứng');
            """;

    public static final String SQL_INSERT_TABLE_CATEGORIES = """
                INSERT INTO Categories (Name) VALUES
                ('Cơm'), ('Bún'), ('Phở'), ('Bánh mì'), ('Chay'), 
                ('Lẩu'), ('Xôi'), ('Mì'), ('Nước');
            """;

    public static final String SQL_INSERT_TABLE_RESTAURANT_CATEGORY = """
                INSERT INTO RestaurantCategory (RestaurantId, CategoryId) VALUES
                (1, 2), -- Bún Chả => Bún
                (2, 3), -- Phở Thìn => Phở
                (3, 4), -- Bánh Mì 25 => Bánh mì
                (4, 5), -- Chay Tâm An => Chay
                (5, 1), -- Cơm Tấm Cali => Cơm
                (6, 2), -- Bún Riêu => Bún
                (7, 6), -- Lẩu Đức Trọc => Lẩu
                (8, 7), -- Xôi Yến => Xôi
                (9, 8), -- Mì Vằn Thắn => Mì
                (10, 9); -- Cafe Giảng => Nước
            """;

    public static final String SQL_INSERT_TABLE_REVIEW_MEDIA = """
                INSERT INTO ReviewMedia (ReviewId, MediaUrl, MediaType) VALUES
                (1, 'https://i.imgur.com/buncha_review.jpg', 'image'),
                (2, 'https://i.imgur.com/pho_review.jpg', 'image'),
                (3, 'https://i.imgur.com/banhmi_review.jpg', 'image'),
                (5, 'https://i.imgur.com/comtam_review.jpg', 'image'),
                (6, 'https://i.imgur.com/bunrieu_review.jpg', 'image'),
                (10, 'https://i.imgur.com/cafetrung_review.jpg', 'image');
            """;


    public static final String SQL_DELETE_TABLE_REVIEWS = """
            DROP TABLE IF EXISTS Reviews;
            """;
    public static final String SQL_DELETE_TABLE_FAVORITES = """
            DROP TABLE IF EXISTS Favorites;            
            """;
    public static final String SQL_DELETE_TABLE_RESTAURANTS = """
            DROP TABLE IF EXISTS Restaurants;            
            """;
    public static final String SQL_DELETE_TABLE_USERS = """
            DROP TABLE IF EXISTS Users;            
            """;
    public static final String SQL_DELETE_TABLE_MENUS = """
                DROP TABLE IF EXISTS Menus;
            """;
    public static final String SQL_DELETE_TABLE_RESTAURANT_CATEGORY = """
                DROP TABLE IF EXISTS RestaurantCategory;
            """;
    public static final String SQL_DELETE_TABLE_CATEGORIES = """
                DROP TABLE IF EXISTS Categories;
            """;

    public static final String SENDER_EMAIL = "daom28659@gmail.com";
    public static final String SENDER_PASSWORD = "qjmi nqkd vdri jfsm";

}
