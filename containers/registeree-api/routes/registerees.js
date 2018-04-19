const express = require("express");
const router = express.Router();
const request = require("request");

const Registerees = require("../models/registeree");

// endpoints for registeree
router.get("/", function(req, res) {
  Registerees.find(function(err, registerees) {
    if (err) {
      res.send(err);
    }
    else {
      res.send(registerees);
    }
  });
});

router.get("/totalUsers", function(req, res) {
  Registerees.count(function(err, count) {
    if (err) {
      res.send(err);
    }
    else {
      res.send({"count":count});
    }
  });
});

router.get("/info/:registereeId", function(req, res) {
  Registerees.findOne(req.params, function(err, registeree) {
    if (err){
      res.send(err);
    } else if (registeree) {
      res.send(registeree);
    } else {
      res.send('Registeree not found...');
    }
  });
});

router.get("/totalCalories", function(req, res) {
  Registerees.aggregate([{ $group:
    { _id: null,
      count: {
        $sum: "$calories"
      }
    }
  }], function(err, totalCalories) {
    if(err) {
      res.send(err);
    } else if (totalCalories) {
      res.send(totalCalories);
    } else {
      res.send("Registeree.calories not found");
    }
  });
});

router.get("/totalSteps", function(req, res) {
  Registerees.aggregate([{ $group:
    { _id: null,
      count: {
        $sum: "$steps"
      }
    }
  }], function(err, totalSteps) {
    if (err) {
      res.send(err);
    } else if (totalSteps) {
      res.send(totalSteps);
    } else {
      res.send("Registeree.steps not found");
    }
  });
});

router.post("/add", function(req, res) {
  // JSON in req.body
  // Insert input validation
  var registeree = req.body
  request.get(process.env.GRAVATAR_URL + "/gravatar/new", function(error, response, body) {
    let json = JSON.parse(body);
    registeree.name = json.name;
    registeree.png = json.png;

    let addRegisteree = new Registerees(registeree);

    addRegisteree.save( function(err) {
      if (err) {
        res.send(err);
      }
      else {
        res.send({name:registeree.name,png:registeree.png});
      }
    });
  })
});


router.post("/update/:registereeId/steps/:steps", function(req, res) {
  // JSON in req.body
  // Insert input validation
  Registerees.update({"registereeId": req.params.registereeId},
    {"steps": req.params.steps}, function(err) {
      if (err) {
        res.send(err);
      } else {
        res.send(`Update Registeree ${req.params.registereeId}'s steps.`);
      }
    });
});


router.post("/update/:registereeId/calories/:calories", function(req, res) {
  // JSON in req.body
  // Insert input validation
  Registerees.update({"registereeId": req.params.registereeId},
    {"calories": req.params.calories}, function(err) {
      if (err) {
        res.send(err);
      } else {
        res.send(`Update Registeree ${req.params.registereeId}'s calories.`);
      }
    });
});

module.exports = router;
