const request = require("supertest");
const app = require("../app");
const expect = require("chai").expect;

describe("User API", () => {
  describe("GET /api/users", () => {
    it("should return a list of users", async () => {
      const res = await request(app)
        .get("/api/users")
        .expect("Content-Type", /json/)
        .expect(200);

      expect(res.body).to.be.an("array");
      expect(res.body).to.have.lengthOf(2);
      expect(res.body[0]).to.have.property("name", "Alice");
    });
  });

  describe("GET /api/users/:id", () => {
    it("should return a user by ID", async () => {
      const res = await request(app)
        .get("/api/users/1")
        .expect("Content-Type", /json/)
        .expect(200);

      expect(res.body).to.have.property("id", 1);
      expect(res.body).to.have.property("name", "Alice");
    });

    it("should return 404 if user not found", async () => {
      await request(app)
        .get("/api/users/999")
        .expect("Content-Type", /json/)
        .expect(404);
    });
  });

  describe("POST /api/users", () => {
    it("should create a new user", async () => {
      const newUser = {
        name: "Charlie",
        email: "charlie@example.com"
      };

      const res = await request(app)
        .post("/api/users")
        .send(newUser)
        .expect("Content-Type", /json/)
        .expect(201);

      expect(res.body).to.have.property("id");
      expect(res.body).to.have.property("name", newUser.name);
      expect(res.body).to.have.property("email", newUser.email);
    });

    it("should return 400 if name or email is missing", async () => {
      const invalidUser = {
        name: "Charlie"
        // missing email
      };

      await request(app)
        .post("/api/users")
        .send(invalidUser)
        .expect("Content-Type", /json/)
        .expect(400);
    });
  });

  describe("PUT /api/users/:id", () => {
    it("should update an existing user", async () => {
      const updatedUser = {
        name: "Updated Name",
        email: "updated@example.com"
      };

      const res = await request(app)
        .put("/api/users/1")
        .send(updatedUser)
        .expect("Content-Type", /json/)
        .expect(200);

      expect(res.body).to.have.property("id", 1);
      expect(res.body).to.have.property("name", updatedUser.name);
      expect(res.body).to.have.property("email", updatedUser.email);

      // Verify the update in the database
      const verifyRes = await request(app)
        .get("/api/users/1")
        .expect(200);

      expect(verifyRes.body).to.deep.equal(res.body);
    });

    it("should return 404 if user to update is not found", async () => {
      const updatedUser = {
        name: "Updated Name",
        email: "updated@example.com"
      };

      await request(app)
        .put("/api/users/999")
        .send(updatedUser)
        .expect("Content-Type", /json/)
        .expect(404);
    });

    it("should return 400 if name or email is missing", async () => {
      const invalidUser = {
        name: "Updated Name"
        // missing email
      };

      await request(app)
        .put("/api/users/1")
        .send(invalidUser)
        .expect("Content-Type", /json/)
        .expect(400);
    });
  });
});