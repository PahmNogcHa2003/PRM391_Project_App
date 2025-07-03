package com.example.prm391_project_apprestaurants.utils;

public class Constant {
    public static final String SQL_CREATE_TABLE_USERS = """
            CREATE TABLE IF NOT EXISTS Users (
                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                Username TEXT NOT NULL UNIQUE,
                Password TEXT NOT NULL,
                Email TEXT,
                Role TEXT DEFAULT 'User', -- 'User' hoặc 'Admin'
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP
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
                CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (UserId) REFERENCES Users(Id),
                FOREIGN KEY (RestaurantId) REFERENCES Restaurants(Id)
            );
            """;
    public static final String SQL_INSERT_TABLE_USERS = """ 
            INSERT INTO Users (Username, Password, Email, Role) VALUES
            ('admin', 'adminpass', 'admin@gmail.com', 'Admin'),
            ('user1', 'userpass1', 'user1@gmail.com', 'User'),
            ('user2', 'userpass2', 'user2@gmail.com', 'User'),
            ('user3', 'userpass3', 'user3@gmail.com', 'User'),
            ('user4', 'userpass4', 'user4@gmail.com', 'User'),
            ('user5', 'userpass5', 'user5@gmail.com', 'User');
            
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
            INSERT INTO Reviews (UserId, RestaurantId, Content) VALUES
            (2, 1, 'Bún chả ngon, không gian sạch sẽ.'),
            (2, 2, 'Phở đậm đà, ăn rất vừa miệng.'),
            (2, 3, 'Bánh mì giòn, pate thơm.'),
            (2, 4, 'Không gian yên tĩnh, đồ ăn chay ngon.'),
            (3, 5, 'Cơm tấm ổn, nước mắm ngon.'),
            (5, 6, 'Bún riêu ngon nhưng hơi đông khách.'),
            (4, 7, 'Lẩu nhiều đồ, phục vụ nhanh.'),
            (3, 8, 'Xôi nhiều topping, ăn rất đã.'),
            (3, 9, 'Mì thủ công sợi mềm.'),
            (4, 10, 'Cafe trứng siêu ngon!');
            
            """;

}
